package com.xjudge.service.contest;

import com.xjudge.entity.*;
import com.xjudge.entity.key.ContestProblemKey;
import com.xjudge.entity.key.UserContestKey;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mapper.ContestMapper;
import com.xjudge.mapper.ProblemMapper;
import com.xjudge.mapper.SubmissionMapper;
import com.xjudge.model.contest.modification.ContestModificationModel;
import com.xjudge.model.contest.modification.ContestCreationModel;
import com.xjudge.model.contest.modification.ContestProblemset;
import com.xjudge.model.problem.ProblemModel;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.repository.ContestProblemRepo;
import com.xjudge.repository.ContestRepo;
import com.xjudge.repository.UserContestRepo;
import com.xjudge.service.problem.ProblemService;
import com.xjudge.service.submission.SubmissionService;
import com.xjudge.service.user.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ContestServiceImp implements ContestService {

    private final ContestRepo contestRepo;
    private final ContestProblemRepo contestProblemRepo;
    private final UserContestRepo userContestRepo;
    private final ContestMapper contestMapper;
    private final ProblemMapper problemMapper;
    private final UserService userService;
    private final ProblemService problemService;
    private final SubmissionService submissionService;
    private final SubmissionMapper submissionMapper;

    @Override
    public Page<Contest> getAllContests(Pageable pageable) {
        return contestRepo.findAll(pageable);
    }

    @Override
    public Contest createContest(@NotNull ContestCreationModel creationModel) {
        User user = userService.getUserByHandle(creationModel.getUserHandle());

        Contest contest = contestMapper.toContest(creationModel);
        contest.setBeginTime(creationModel.getBeginTime()); // Set when creating only

        contestRepo.save(contest);

        //TODO: handle the group relation
        handleContestProblemSetRelation(creationModel.getProblems(), contest);
        handleContestUserRelation(user, contest);

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
    public Contest updateContest(Long id, ContestModificationModel updatingModel) {
        if (!contestRepo.existsById(id)) {
            throw new XJudgeException("There's no contest with this id = " + id, ContestServiceImp.class.getName(), HttpStatus.NOT_FOUND);
        }

        User user = userService.getUserByHandle(updatingModel.getUserHandle());

        Contest contest = contestMapper.toContest(updatingModel);
        contest.setId(id);

        contestRepo.save(contest);

        //TODO: handle the group relation
        handleContestProblemSetRelation(updatingModel.getProblems(), contest);
        handleContestUserRelation(user, contest);

        return contest;
    }

    @Override
    public void deleteContest(Long id) {
        contestRepo.deleteById(id);
    }

    @Override
    public List<ProblemModel> getContestProblems(Long id) {
        Contest contest = getContest(id);

        List<ProblemModel> problems = new ArrayList<>(contest.getProblemSet().stream()
                .map(contestProblem -> {
                    Problem problem = contestProblem.getProblem();
                    String problemHashtag = contestProblem.getProblemHashtag();
                    return problemMapper.toModel(problem, problemHashtag);
                }).toList());

        problems.sort(Comparator.comparing(ProblemModel::problemHashtag));

        return problems;
    }

    @Override
    public SubmissionModel submitInContest(Long id, SubmissionInfoModel info) {
        Contest contest = getContest(id);
        Submission submission = problemService.submit(info);

        submission.setContest(contest);

        //TODO: update contest rank

        submission = submissionService.save(submission);

        return submissionMapper.toModel(submission);
    }


    private void handleContestProblemSetRelation(List<ContestProblemset> problemSet, Contest contest) {
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

            contestProblemRepo.save(contestProblem);
        }
    }

    private void handleContestUserRelation(User user, Contest contest) {
        UserContestKey userContestKey = new UserContestKey(user.getId(), contest.getId());
        UserContest userContest = UserContest.builder()
                .id(userContestKey)
                .contest(contest)
                .user(user)
                .isOwner(true)
                .build();

        userContestRepo.save(userContest);
    }

}
