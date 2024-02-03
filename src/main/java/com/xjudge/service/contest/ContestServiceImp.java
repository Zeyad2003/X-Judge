package com.xjudge.service.contest;

import com.github.dockerjava.api.exception.BadRequestException;
import com.xjudge.entity.*;
import com.xjudge.model.contest.ContestData;
import com.xjudge.model.enums.UserContestRole;
import com.xjudge.mappers.ContestMapper;
import com.xjudge.model.contest.ContestCreationRequest;
import com.xjudge.model.contest.ContestModel;
import com.xjudge.model.contest.ContestProblemData;
import com.xjudge.model.problem.ContestProblemResp;
import com.xjudge.repository.ContestRepo;
import com.xjudge.repository.GroupRepo;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContestServiceImp implements ContestService{

    ContestRepo contestRepo;
    UserRepo userRepo;
    GroupRepo groupRepo;
    ProblemRepository problemRepo;

    ContestMapper mapper;

    @Autowired
    public ContestServiceImp(ContestRepo contestRepo, UserRepo userRepo , GroupRepo groupRepo , ProblemRepository problemRepo , ContestMapper mapper) {
        this.contestRepo = contestRepo;
        this.userRepo = userRepo;
        this.groupRepo = groupRepo;
        this.problemRepo = problemRepo;
        this.mapper = mapper;
    }

    /*
     1- create UserContest entity after changing the relation
     2- do logic of properties
     */
    @Override
    public ContestData createContest(@NotNull ContestCreationRequest contest) {
        User user = userRepo.findById(contest.getUserId()).orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        Group group = groupRepo.findById(contest.getGroupId()).orElseThrow(() -> new EntityNotFoundException("GROUP_NOT_FOUND"));


        if(!isUserInGroup(user , group.getGroupUsers()))
        {
            throw new BadRequestException("BAD REQUEST");
        }
//
//        if(contest.getContestBeginTime().toEpochMilli() - System.currentTimeMillis() <= 0){
//            throw new BadRequestException("BEGIN TIME SHOULD BE AFTER CURRENT TIME");
//        }



        Contest contest1 = new Contest();
        contest1.setProblemSet(getContestProblemData(contest.getProblems() , contest1));
        contest1.setContestDescription(contest.getContestDescription());
        contest1.setContestLength(contest.getContestLength());
        contest1.setContestTitle(contest.getContestTitle());
        contest1.setContestVisibility(contest.getContestVisibility());
        contest1.setContestType(contest.getContestType());
        contest1.setContestBeginTime(contest.getContestBeginTime());


        group.getGroupContests().add(contest1);
        contest1 = contestRepo.save(contest1);

        UserContest userContest = new UserContest(contest1 , user , false , UserContestRole.OWNER);
        user.getContests().add(userContest);

        userRepo.save(user);

        return mapper.toContestDataResp(contest1);
    }

    @Override
    public List<ContestData> getAllContests() {
        return contestRepo.findAll()
                .stream()
                .map(contest -> mapper.toContestDataResp(contest))
                .toList();
    }

    @Override
    public ContestData getContest(Long id) {
        return mapper.toContestDataResp(
                contestRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("CONTEST_NOT_FOUND"))
        );
    }

    @Override
    public ContestModel updateContest(Long id , ContestModel model) {
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
                                .problemId(contestProblem.getProblem().getProblemId())
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
}