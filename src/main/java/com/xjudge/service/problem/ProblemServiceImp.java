package com.xjudge.service.problem;

import com.xjudge.entity.Problem;
import com.xjudge.entity.Submission;
import com.xjudge.entity.User;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mapper.UserMapper;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.problem.ProblemsPageModel;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.repository.ProblemRepository;
import com.xjudge.service.scraping.GetProblemAutomation;
import com.xjudge.service.scraping.atcoder.AtCoderSubmission;
import com.xjudge.service.scraping.codeforces.CodeforcesSubmission;
import com.xjudge.service.submission.SubmissionService;
import com.xjudge.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProblemServiceImp implements ProblemService {

    private final ProblemRepository problemRepo;
    private final GetProblemAutomation codeforcesGetProblem;
    private final GetProblemAutomation atCoderGetProblem;
    private final CodeforcesSubmission codeforcesSubmission;
    private final AtCoderSubmission atCoderSubmission;
    private final SubmissionService submissionService;
    private final UserService userService;
    private final UserMapper mapper;


    @Override
    public Page<ProblemsPageModel> getAllProblems(Pageable pageable) {
        Page<Problem> problemList = problemRepo.findAll(pageable);
        return problemList.map(problem -> ProblemsPageModel.builder()
                .oj(problem.getSource())
                .problemCode(problem.getProblemCode())
                .problemTitle(problem.getTitle())
                .problemLink(problem.getProblemLink())
                .contestName(problem.getExtraInfo().get("contestName").toString())
                .contestLink(problem.getContestLink())
                .solvedCount(submissionService.getSolvedCount(problem.getProblemCode(), OnlineJudgeType.CodeForces))
                .build());
    }

    @Override
    public Problem getProblemByCodeAndSource(String problemCode, String problemSource) {
        if (problemSource.equalsIgnoreCase("codeforces")) {
            Optional<Problem> problem = problemRepo.findByProblemCodeAndSource(problemCode, OnlineJudgeType.CodeForces);
            return problem.orElseGet(() -> scrapCodeForcesProblem(problemCode));
        }

        if (problemSource.equalsIgnoreCase("atcoder")) {
            Optional<Problem> problem = problemRepo.findByProblemCodeAndSource(problemCode, OnlineJudgeType.AtCoder);
            return problem.orElseGet(() -> scrapAtCoderProblem(problemCode));
        }

        throw new XJudgeException("Online Judge not supported yet.", ProblemServiceImp.class.getName(), HttpStatus.NOT_FOUND);
    }

    @Override
    public Submission submit(SubmissionInfoModel info , Authentication authentication) {
        User user = mapper.toEntity(userService.findByHandle(authentication.getName()));
        if (info.ojType() == OnlineJudgeType.CodeForces) {
            Problem problem = getProblemByCodeAndSource(info.problemCode(), info.ojType().name());
            Submission submission = codeforcesSubmission.submit(info);
            submission.setProblem(problem);
            user.setAttemptedCount(user.getAttemptedCount()+1);
            if(submission.getSubmissionStatus().equalsIgnoreCase("accepted")){
                user.setSolvedCount(user.getSolvedCount()+1);
            }
            user = userService.save(user);
            submission.setUser(user);
            return submissionService.save(submission);

        } else if (info.ojType() == OnlineJudgeType.AtCoder) {
            Problem problem = getProblemByCodeAndSource(info.problemCode(), info.ojType().name());
            System.out.println(problem);
            Submission submission = atCoderSubmission.submit(info);
            submission.setProblem(problem);
            user.setAttemptedCount(user.getAttemptedCount()+1);
            if(submission.getSubmissionStatus().equalsIgnoreCase("AC")){
                user.setSolvedCount(user.getSolvedCount()+1);
            }
            user =userService.save(user);
            submission.setUser(user);
            return submissionService.save(submission);
        }

        throw new XJudgeException("Online Judge not supported yet.", ProblemServiceImp.class.getName(), HttpStatus.NOT_FOUND);
    }

    @Override
    public Page<ProblemsPageModel> searchByTitle(String title, Pageable pageable) {
        Page<Problem> problemList = problemRepo.findByTitleContaining(title, pageable);
        return problemList.map(problem -> ProblemsPageModel.builder()
                .oj(OnlineJudgeType.CodeForces)
                .problemCode(problem.getProblemCode())
                .problemTitle(problem.getTitle())
                .contestName(problem.getExtraInfo().get("contestName").toString())
                .solvedCount(submissionService.getSolvedCount(problem.getProblemCode(), OnlineJudgeType.CodeForces))
                .build());
    }

    @Override
    public Page<ProblemsPageModel> searchBySource(String source, Pageable pageable) {
        Page<Problem> problemList = problemRepo.findBySourceContaining(OnlineJudgeType.valueOf(source), pageable);
        return problemList.map(problem -> ProblemsPageModel.builder()
                .oj(OnlineJudgeType.CodeForces)
                .problemCode(problem.getProblemCode())
                .problemTitle(problem.getTitle())
                .contestName(problem.getExtraInfo().get("contestName").toString())
                .solvedCount(submissionService.getSolvedCount(problem.getProblemCode(), OnlineJudgeType.CodeForces))
                .build());
    }

    @Override
    public Page<ProblemsPageModel> searchByProblemCode(String problemCode, Pageable pageable) {
        Page<Problem> problemList = problemRepo.findByProblemCodeContaining(problemCode, pageable);
        return problemList.map(problem -> ProblemsPageModel.builder()
                .oj(OnlineJudgeType.CodeForces)
                .problemCode(problem.getProblemCode())
                .problemTitle(problem.getTitle())
                .contestName(problem.getExtraInfo().get("contestName").toString())
                .solvedCount(submissionService.getSolvedCount(problem.getProblemCode(), OnlineJudgeType.CodeForces))
                .build());
    }

    private Problem scrapCodeForcesProblem(String problemCode) {
        String contestId = problemCode.replaceAll("(\\d+).*", "$1");
        String problemId = problemCode.replaceAll("\\d+(.*)", "$1");

        Problem problem = codeforcesGetProblem.GetProblem(contestId, problemId);
        problem.setSubmissions(new HashSet<>());
        problem.setUserProblems(new HashSet<>());
        problem.setContests(new HashSet<>());

//        Optional<Problem> storedProblem = problemRepo.findByProblemCodeAndSource(problemCode, OnlineJudgeType.CodeForces);
//        storedProblem.ifPresent(value -> problem.setId(value.getId()));

        problem = problemRepo.save(problem);

        return problem;
    }

    private Problem scrapAtCoderProblem(String problemCode) {
        String[] cpCode = problemCode.split("_");
        StringBuilder contestIdBuilder = new StringBuilder();
        for (int i = 0; i < cpCode.length - 1; i++) {
            if (i > 0) {
                contestIdBuilder.append('-');
            }
            contestIdBuilder.append(cpCode[i]);
        }
        String contestId = contestIdBuilder.toString();

        Problem problem = atCoderGetProblem.GetProblem(contestId, problemCode);
        Optional<Problem> storedProblem = problemRepo.findByProblemCodeAndSource(problemCode, OnlineJudgeType.AtCoder);
        storedProblem.ifPresent(value -> problem.setId(value.getId()));

        problemRepo.save(problem);
        return problem;
    }
}
