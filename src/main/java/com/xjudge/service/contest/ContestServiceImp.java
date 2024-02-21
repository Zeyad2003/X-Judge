package com.xjudge.service.contest;

import com.xjudge.entity.*;
import com.xjudge.entity.key.ContestProblemKey;
import com.xjudge.entity.key.UserContestKey;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mapper.ContestMapper;
import com.xjudge.model.contest.ContestCreationModel;
import com.xjudge.model.contest.ContestProblemset;
import com.xjudge.model.contest.ContestUpdatingModel;
import com.xjudge.repository.ContestProblemRepo;
import com.xjudge.repository.ContestRepo;
import com.xjudge.repository.UserContestRepo;
import com.xjudge.service.problem.ProblemService;
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
    private final UserService userService;
    private final ProblemService problemService;

    @Override
    public Page<Contest> getAllContests(Pageable pageable) {
        return contestRepo.findAll(pageable);
    }

    @Override
    public Contest createContest(@NotNull ContestCreationModel creationModel) {
        User user = userService.getUserByHandle(creationModel.getOwnerHandle());

        Contest contest = contestMapper.toContest(creationModel);
        contest.setBeginTime(creationModel.getBeginTime()); // Set when creating only

        contestRepo.save(contest);

        //TODO: handle the group relation
        handleContestProblemsetRelation(creationModel.getProblems(), contest);
        handleContestUserRelation(user, contest);

        return contest;
    }

    // TODO: use model after implementing it
    @Override
    public Contest getContest(Long id) {
        Optional<Contest> contestOptional = contestRepo.findById(id);

        if(contestOptional.isEmpty()) {
            throw new XJudgeException("There's no contest with this id = " + id, ContestServiceImp.class.getName(), HttpStatus.NOT_FOUND);
        }

        return contestOptional.get();
    }

    @Override
    public Contest updateContest(Long id, ContestUpdatingModel updatingModel) {
        if(!contestRepo.existsById(id)) {
            throw new XJudgeException("There's no contest with this id = " + id, ContestServiceImp.class.getName(), HttpStatus.NOT_FOUND);
        }

        User user = userService.getUserByHandle(updatingModel.getOwnerHandle());

        Contest contest = contestMapper.toContest(updatingModel);
        contest.setId(id);

        contestRepo.save(contest);

        //TODO: handle the group relation
        handleContestProblemsetRelation(updatingModel.getProblems(), contest);
        handleContestUserRelation(user, contest);

        return contest;
    }

    @Override
    public void deleteContest(Long id) {
        contestRepo.deleteById(id);
    }
/*
    @Override
    public List<ContestUpdatingModel> getAllContests() {
        return contestRepo.findAll()
                .stream()
                .map(contest -> mapper.toContestDataResp(contest))
                .toList();
    }

    @Override
    public ContestUpdatingModel getContest(Long id) {
        return mapper.toContestDataResp(
                contestRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("CONTEST_NOT_FOUND"))
        );
    }

    @Override
    public ContestUserDataModel updateContest(Long id , ContestUserDataModel model) {
        Contest contest = contestRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("CONTEST_NOT_FOUND"));

        contest.setContestDescription(model.getContestDescription());
        contest.setContestLength(model.getContestLength());
        contest.setContestTitle(model.getContestTitle());
        contest.setContestVisibility(model.getContestVisibility());
        contest.setContestType(model.getContestType());
        contest.setContestBeginTime(model.getContestBeginTime());

        return mapper.toModel(contestRepo.save(contest));
    }

    @Override
    public void deleteContest(Long id) {
        Contest contest = contestRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("CONTEST_NOT_FOUND"));
        contestRepo.delete(contest);
    }

    @Override
    public List<ContestProblemResp> gerContestProblems(Long id) {
        Contest contest = contestRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("CONTEST_NOT_FOUND"));
        return contest.getProblemSet()
                .stream()
                .map(contestProblem ->
                        ContestProblemResp.builder()
                                .problemId(contestProblem.getProblem().getId())
                                .problemCode(contestProblem.getCode())
                                .platform(contestProblem.getProblem().getProblemSource())
                                .title(contestProblem.getAlias())
                                .build()
                        )
                .toList();
    }

    private List<ContestProblem> getContestProblemData(List<ContestProblemData> problemSet , Contest contest){
        return problemSet.stream()
                .map((contestProblemData -> contestProblemMapper(contestProblemData , contest)))
                .collect(Collectors.toList());
    }

    private ContestProblem contestProblemMapper(ContestProblemData data , Contest contest){

        Problem problem = problemRepo.findById(data.getProblemId())
                .orElseThrow(() -> new EntityNotFoundException("PROBLEM_NOT_FOUND"));

        ContestProblem contestProblem = mapper.toContestProblem(data);
        contestProblem.setProblem(problem);
        contestProblem.setContest(contest);


        return contestProblem;
    }

    private boolean isUserInGroup(User user , List<User> users) {
        return users.stream()
                .anyMatch(user1 -> user1.equals(user));
    }
    */

    private void handleContestProblemsetRelation(List<ContestProblemset> problemset, Contest contest) {
        contestProblemRepo.deleteAllByContestId(contest.getId());
        for (ContestProblemset problemaya : problemset) {
            String code = problemaya.ojType() + "-" + problemaya.problemCode();
            Problem problem = problemService.getProblemByCode(code);

            ContestProblemKey contestProblemKey = new ContestProblemKey(contest.getId(), problem.getId());

            ContestProblem contestProblem = ContestProblem.builder()
                    .id(contestProblemKey)
                    .contest(contest)
                    .problem(problem)
                    .problemWeight(problemaya.problemWeight())
                    .problemAlias(problemaya.problemAlias())
                    .problemCode(problemaya.problemCode())
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
