package com.xjudge.service.contest;

import com.xjudge.entity.*;
import com.xjudge.entity.key.ContestProblemKey;
import com.xjudge.entity.key.UserContestKey;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mapper.*;
import com.xjudge.model.contest.ContestPageModel;
import com.xjudge.model.contest.ContestRankModel;
import com.xjudge.model.contest.ContestRankSubmission;
import com.xjudge.model.contest.modification.ContestClientRequest;
import com.xjudge.model.contest.modification.ContestProblemset;
import com.xjudge.model.enums.ContestStatus;
import com.xjudge.model.enums.ContestType;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.repository.ContestProblemRepo;
import com.xjudge.repository.ContestRepo;
import com.xjudge.service.contest.usercontest.UserContestService;
import com.xjudge.service.group.GroupService;
import com.xjudge.service.problem.ProblemService;
import com.xjudge.service.submission.SubmissionService;
import com.xjudge.service.user.UserService;
import com.xjudge.util.ContestantComparator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.BiFunction;
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
    private final UserContestService userContestService;
    private final UserContestMapper userContestMapper;
     private final Logger logger = LoggerFactory.getLogger(ContestServiceImp.class);

    @Override
    public Page<ContestPageModel>  getAllContests(Pageable pageable) {
        Page<Contest> contests = contestRepo.findAll(pageable);
        return contests.map(
                contestMapper::toContestPageModel
        );
    }


    @Override
    public Contest createContest(ContestClientRequest creationModel , Authentication authentication) {
        if(authentication.getName() == null) {
            throw new XJudgeException("un authenticated user" , ContestServiceImp.class.getName() , HttpStatus.UNAUTHORIZED);
        }


        Contest contest = mappingBasedOnContestType(creationModel);

        contest.setBeginTime(creationModel.getBeginTime()); // Set when creating only
        contest.setUsers(new HashSet<>());
        contest.setProblemSet(new HashSet<>()) ;
        contestRepo.save(contest);

        User user = userMapper.toEntity(userService.findByHandle(authentication.getName()));

        handleContestProblemSetRelation(creationModel.getProblems(), contest);
        handleContestUserRelation(user, contest , true , false);

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

        Contest contest = mappingBasedOnContestType(updatingModel);

        contest.setId(id);
        contest.setUsers(contestOptional.get().getUsers());
        contest.setProblemSet(new HashSet<>());
        contest.setBeginTime(contestOptional.get().getBeginTime());

        if(authentication.getName() == null) {
            throw new XJudgeException("un authenticated user" , ContestServiceImp.class.getName() , HttpStatus.UNAUTHORIZED);
        }

        User user = userMapper.toEntity(userService.findByHandle(authentication.getName()));


        handleContestProblemSetRelation(updatingModel.getProblems(), contest);

        if(!userContestService.existsById(new UserContestKey(user.getId() , contest.getId()))) {
            handleContestUserRelation(user, contest, true, false);
        }


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
    public SubmissionModel submitInContest(Long id, SubmissionInfoModel info , Authentication authentication) {
        Contest contest = getContest(id);
        User user = userMapper.toEntity(userService.findByHandle(authentication.getName()));
        ContestStatus contestStatus = checkContestStatus(contest);

        if(contestStatus == ContestStatus.SCHEDULED){
            throw new XJudgeException("The contest has not started yet" , ContestServiceImp.class.getName() , HttpStatus.BAD_REQUEST);
        }

        if(!contestProblemRepo.existsByProblemCodeAndContestId(info.problemCode() , id)){
            throw new XJudgeException("No such problem with this code in contest" , ContestServiceImp.class.getName() , HttpStatus.BAD_REQUEST);
        }

        UserContest userContest = getUserContest(contest , user.getHandle());

        if(!userContest.getIsParticipant()){
            logger.info("Enter In participation part !");
            handleContestUserRelation(user , contest , userContest.getIsOwner(), true);
            contestRepo.save(contest);
            userContest = getUserContest(contest , user.getHandle());
        }

        Submission submission = problemService.submit(info, authentication);

       logger.info("isProblemAccepted : " + isProblemAcceptedByUser(contest.getId() , user.getId() , info.problemCode()));
        if(submission.getVerdict().equals("Accepted") && !isProblemAcceptedByUser(contest.getId() , user.getId() , info.problemCode())){
            Duration duration = Duration.between(contest.getBeginTime() , submission.getSubmitTime());
            userContest.setUserContestPenalty(userContest.getUserContestPenalty() + duration.getSeconds());
            userContest.setUserContestScore(userContest.getUserContestScore() + getProblemContestScore(contest , info.problemCode()));
            userContest.setNumOfAccepted(userContest.getNumOfAccepted() + 1);
        } else if (submission.getVerdict().startsWith("W")) {
            userContest.setUserContestPenalty(userContest.getUserContestPenalty() + 20 * 60);
        }

        contest.getUsers().add(userContest);
        submission.setContest(contest);
        submission = submissionService.save(submission);

        return submissionMapper.toModel(submission);
    }

    @Override
    public List<SubmissionModel> getContestSubmissions(Long id) {
        Contest contest = getContest(id);
        List<Submission> submissions = submissionService.getSubmissionsByContestId(contest.getId());

        return submissionMapper.toModels(submissions);
    }

    @Override
    public List<ContestRankModel> getRank(Long contestId) {
        Contest contest = getContest(contestId);

        return contest.getUsers()
                .stream().filter(UserContest::getIsParticipant)
                .map(userContest ->
                        userContestMapper.toContestRankModel(userContest , getContestRankModel(userContest , contest))
                       )
                .sorted(new ContestantComparator())
                .toList();

    }

    private List<ContestRankSubmission> getContestRankModel (UserContest userContest , Contest contest){
        return submissionService.getSubmissionsByContestIdAndUserId(contest.getId(), userContest.getUser().getId())
                .stream()
                .map(submission ->
                        userContestMapper.toContestRankSubmissionModel(submission , getProblemIndex(contest , submission.getProblem().getProblemCode()) , contest.getBeginTime() , submission.getSubmitTime())
                )
                .sorted(Comparator.comparing(ContestRankSubmission::getProblemIndex))
                .toList()
                ;
    };


    private String getProblemIndex(Contest contest , String problemCode){
        return contest.getProblemSet()
                .stream()
                .filter(contestProblem -> contestProblem.getProblemCode().equals(problemCode))
                .findFirst()
                .orElseThrow(() -> new XJudgeException("PROBLEM_NOT_Found" , ContestServiceImp.class.getName() , HttpStatus.INTERNAL_SERVER_ERROR))
                .getProblemHashtag();
    };


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

    public void handleContestUserRelation(User user, Contest contest , boolean isOwner , boolean isParticipant) {
        UserContestKey userContestKey = new UserContestKey(user.getId(), contest.getId());
        UserContest userContest = UserContest.builder()
                .id(userContestKey)
                .contest(contest)
                .user(user)
                .isOwner(isOwner)
                .isFavorite(false)
                .isParticipant(isParticipant)
                .numOfAccepted(0)
                .userContestRank(0)
                .userContestScore(0)
                .userContestPenalty(0L)
                .build();

        contest.getUsers().add(userContest);
    }




    private boolean checkIfProblemHashtagDuplicated(List<ContestProblemset> problemset){
        return problemset.stream()
                .map(contestProblemset -> contestProblemset.problemHashtag().toUpperCase())
                .collect(Collectors.toSet())
                .size() == problemset.size();
    }

    private Contest mappingBasedOnContestType(ContestClientRequest model){
        if(model.getType() == ContestType.CLASSIC) {
            return contestMapper.toContestClassical(model);
        }

        Contest contest = contestMapper.toContestGroup(model);
        // handle contest relation
        Group group = groupMapper.toEntity(groupService.getSpecificGroup(model.getGroupId()));
        contest.setGroup(group);

        return contest;
    }

    private ContestStatus checkContestStatus(Contest contest){
        Instant currentTime = Instant.now();
        if(currentTime.isBefore(contest.getBeginTime())){
            return ContestStatus.SCHEDULED;
        } else if(currentTime.isBefore(contest.getBeginTime().plus(contest.getDuration()))){
            return ContestStatus.RUNNING;
        }
        return ContestStatus.ENDED;
    }

    private UserContest getUserContest(Contest contest , String handle){
        return contest.getUsers()
                .stream()
                .filter(userContest -> userContest.getUser().getHandle().equals(handle))
                .findFirst()
                .orElseGet(() -> UserContest.builder()
                        .isParticipant(false)
                        .isOwner(false)
                        .build());
    }

    private Integer getProblemContestScore(Contest contest , String problemCode){
        return contest.getProblemSet()
                .stream()
                .filter(contestProblem -> contestProblem.getProblemCode().equals(problemCode))
                .findFirst()
                .orElseThrow().getProblemWeight();
    }



    private boolean isProblemAcceptedByUser(long contestId , long userId , String problemCode){
        return submissionService.getSubmissionsByContestId(contestId)
                .stream()
                .anyMatch(submission ->submission.getVerdict().equals("Accepted") &&
                        submission.getProblem().getProblemCode().equals(problemCode) &&
                        submission.getUser().getId().equals(userId));
    }
}
