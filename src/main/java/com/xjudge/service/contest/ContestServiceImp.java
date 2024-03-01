package com.xjudge.service.contest;

import com.xjudge.entity.*;
import com.xjudge.entity.key.ContestProblemKey;
import com.xjudge.entity.key.UserContestKey;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mapper.*;
import com.xjudge.model.contest.modification.ContestClientRequest;
import com.xjudge.model.contest.modification.ContestProblemset;
import com.xjudge.model.enums.ContestType;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.repository.ContestProblemRepo;
import com.xjudge.repository.ContestRepo;
import com.xjudge.service.group.GroupService;
import com.xjudge.service.problem.ProblemService;
import com.xjudge.service.submission.SubmissionService;
import com.xjudge.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestServiceImp implements ContestService {

    private final ContestRepo contestRepo;
    private final ContestProblemRepo contestProblemRepo;
    private final ContestMapper contestMapper;
    private final ProblemMapper problemMapper;
    private final UserService userService;
    private final ProblemService problemService;
    private final SubmissionService submissionService;
    private final SubmissionMapper submissionMapper;
    private final UserMapper userMapper;
    private final GroupService groupService;
    private final GroupMapper groupMapper;


    @Override
    public Page<Contest> getAllContests(Pageable pageable) {
        return contestRepo.findAll(pageable);
    }

    @Override
    public Contest createContest(ContestClientRequest creationModel , Authentication authentication) {
        if(authentication.getName() == null) {
            throw new XJudgeException("un authenticated user" , ContestServiceImp.class.getName() , HttpStatus.UNAUTHORIZED);
        }


        Contest contest = null;
        if(creationModel.getType() == ContestType.CLASSIC) {
             contest = contestMapper.toContestClassical(creationModel);
        }else{
             contest = contestMapper.toContestGroup(creationModel);
             Group group = groupMapper.toEntity(groupService.getSpecificGroupByName(creationModel.getGroupName()));
             contest.setGroup(group);
        }

        contest.setBeginTime(creationModel.getBeginTime()); // Set when creating only
        contest.setUsers(new HashSet<>());
        contest.setProblemSet(new HashSet<>()) ;
        contestRepo.save(contest);

        User user = userMapper.toEntity(userService.findByHandle(authentication.getName()));

        handleContestProblemSetRelation(creationModel.getProblems(), contest);
        handleContestUserRelation(user, contest);

        contestRepo.save(contest);

        return contest;
    }



    // TODO: use model after implementing it
    @Override
    public Contest getContest(Long id) {
        Optional<Contest> contestOptional = contestRepo.findById(id);

        if (contestOptional.isEmpty()) {
            throw new XJudgeException("There's no contest with this id = " + id, ContestServiceImp.class.getName(), HttpStatus.NOT_FOUND);
        }

        return contestOptional.get();
    }

    @Override
    public Contest updateContest(Long id, ContestClientRequest updatingModel , Authentication authentication) {
        Optional<Contest> contestOptional = contestRepo.findById(id);

        if(contestOptional.isEmpty()){
            throw new XJudgeException("There's no contest with this id = " + id, ContestServiceImp.class.getName(), HttpStatus.NOT_FOUND);
        }

        Contest contest = contestMapper.toContestClassical(updatingModel);
        contest.setId(id);
        contest.setUsers(contestOptional.get().getUsers());
        contest.setProblemSet(new HashSet<>());
        contest.setBeginTime(contestOptional.get().getBeginTime());

        if(authentication.getName() == null) {
            throw new XJudgeException("un authenticated user" , ContestServiceImp.class.getName() , HttpStatus.UNAUTHORIZED);
        }

        User user = userMapper.toEntity(userService.findByHandle(authentication.getName()));


        //TODO: handle the group relation
        handleContestProblemSetRelation(updatingModel.getProblems(), contest);
        handleContestUserRelation(user, contest);


        return contestRepo.save(contest);
    }

    @Override
    public void deleteContest(Long id) {
        if(!contestRepo.existsById(id)){
            throw new XJudgeException("There's no contest with this id = " + id, ContestServiceImp.class.getName(), HttpStatus.NOT_FOUND);
        }
        contestRepo.deleteById(id);
    }

    @Override
    public List<ProblemModel> getContestProblems(Long id) {
        Contest contest = getContest(id);

        List<ProblemModel> problems = new ArrayList<>(
                contest.getProblemSet()
                        .stream()
                        .map(contestProblem -> problemMapper.toModel(contestProblem.getProblem() , contestProblem.getProblemHashtag()))
                        .toList());

        problems.sort(Comparator.comparing(ProblemModel::problemHashtag));

        return problems;
    }

    @Override
    public ProblemModel getContestProblem(Long id, String problemHashtag) {
        if (!contestProblemRepo.existsByProblemHashtagAndContestId(problemHashtag , id)) {
            throw new XJudgeException("There's no problem with this hashtag = " + problemHashtag, ContestServiceImp.class.getName(), HttpStatus.NOT_FOUND);
        }

        ContestProblem contestProblem =  contestProblemRepo.findContestProblemByProblemHashtagAndContestId(problemHashtag , id);

        ProblemModel problemModel = problemMapper.toModel(
               contestProblem.getProblem(),
                problemHashtag
        );

        handleSpoilerData(problemModel, contestProblem.getContest());

        return problemModel;
    }

    //TODO: implement this method
    private void handleSpoilerData(ProblemModel problemModel, Contest contest) {
        // if contest still running, remove
    }

    @Override
    public SubmissionModel submitInContest(Long id, SubmissionInfoModel info) {
        Contest contest = getContest(id);

        if(!contestProblemRepo.existsByProblemCodeAndContestId(info.problemCode() , id)){
            throw new XJudgeException("No such problem with this code in contest" , ContestServiceImp.class.getName() , HttpStatus.BAD_REQUEST);
        }

        Submission submission = problemService.submit(info);

        submission.setContest(contest);

        //TODO: update contest rank

        submission = submissionService.save(submission);

        return submissionMapper.toModel(submission);
    }

    @Override
    public List<SubmissionModel> getContestSubmissions(Long id) {
        Contest contest = getContest(id);
        List<Submission> submissions = submissionService.getSubmissionsByContestId(contest.getId());

        return submissionMapper.toModels(submissions);
    }


    private void handleContestProblemSetRelation(List<ContestProblemset> problemSet, Contest contest) {
        if(!checkIfProblemHashtagDuplicated(problemSet)){
            throw new XJudgeException("problemHashtag should not be duplicated" , ContestServiceImp.class.getName(), HttpStatus.BAD_REQUEST);
        }

        contestProblemRepo.deleteAllByContestId(contest.getId());

        for (ContestProblemset problemaya : problemSet) {
            Problem problem = problemService.getProblemByCodeAndSource(problemaya.problemCode(), problemaya.ojType().name());

            ContestProblemKey contestProblemKey = new ContestProblemKey(contest.getId(), problem.getId());

            ContestProblem contestProblem = ContestProblem.builder()
                    .id(contestProblemKey)
                    .contest(contest)
                    .problem(problem)
                    .problemWeight(problemaya.problemWeight())
                    .problemAlias(problemaya.problemAlias())
                    .problemCode(problemaya.problemCode())
                    .problemHashtag(problemaya.problemHashtag())
                    .build();

            contest.getProblemSet().add(contestProblem);
        }
    }

    private void handleContestUserRelation(User user, Contest contest) {
        UserContestKey userContestKey = new UserContestKey(user.getId(), contest.getId());
        UserContest userContest = UserContest.builder()
                .id(userContestKey)
                .contest(contest)
                .user(user)
                .isOwner(true)
                .isFavorite(false)
                .isParticipant(false)
                .build();

        contest.getUsers().add(userContest);
    }


    private boolean checkIfProblemHashtagDuplicated(List<ContestProblemset> problemset){
        return problemset.stream()
                .map(contestProblemset -> contestProblemset.problemHashtag().toUpperCase())
                .collect(Collectors.toSet())
                .size() == problemset.size();
    }

}
