package com.xjudge.service.submission;

import com.xjudge.entity.Compiler;
import com.xjudge.entity.Problem;
import com.xjudge.entity.Submission;
import com.xjudge.entity.User;
import com.xjudge.exception.XJudgeException;
import com.xjudge.mapper.SubmissionMapper;
import com.xjudge.model.contest.ContestStatusPageModel;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.model.submission.SubmissionPageModel;
import com.xjudge.repository.ContestRepo;
import com.xjudge.repository.SubmissionRepo;
import com.xjudge.service.contest.contestproblem.ContestProblemService;
import com.xjudge.service.problem.ProblemService;
import com.xjudge.service.scraping.strategy.SubmissionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepo submissionRepo;
    private final SubmissionMapper submissionMapper;
    private final ContestProblemService contestProblemService;
    private final Map<OnlineJudgeType, SubmissionStrategy> submissionStrategies;


    @Autowired
    public SubmissionServiceImpl(SubmissionRepo submissionRepo, SubmissionMapper submissionMapper, ContestProblemService contestProblemService, Map<OnlineJudgeType, SubmissionStrategy> submissionStrategies, ContestRepo contestRepo) {
        this.submissionRepo = submissionRepo;
        this.submissionMapper = submissionMapper;
        this.contestProblemService = contestProblemService;
        this.submissionStrategies = submissionStrategies;
    }

    @Override
    public SubmissionModel getSubmissionById(Long submissionId , Authentication authentication) {
        Submission submission = submissionRepo.findById(submissionId).orElseThrow(() -> new XJudgeException("Submission not found." , SubmissionServiceImpl.class.getName() , HttpStatus.NOT_FOUND));
        if(submission.getSubmissionStatus().equalsIgnoreCase("submitted")) {
          return determineSubmissionModel(submission , authentication);
        }
        SubmissionInfoModel submissionInfoModel = getSubmissionInfo(submission);
        SubmissionStrategy strategy = submissionStrategies.get(submissionInfoModel.ojType());
        Submission updatedSubmission = strategy.submit(submissionInfoModel);
        updateSubmissionStatus(submission, updatedSubmission);
        return determineSubmissionModel(submissionRepo.save(submission) , authentication);
    }

    @Override
    public void resubmit(Submission submission) {
        SubmissionInfoModel submissionInfoModel = getSubmissionInfo(submission);
        SubmissionStrategy strategy = submissionStrategies.get(submissionInfoModel.ojType());
        Submission updatedSubmission = strategy.submit(submissionInfoModel);
        updateSubmissionStatus(submission, updatedSubmission);
        submissionRepo.save(submission);
    }

    @Override
    public Page<SubmissionPageModel> getAllSubmissions(Pageable pageable) {
        Page<Submission> submissions = submissionRepo.findAll(pageable);
        return submissions.map(submission ->
            submissionMapper.toPageModel(submission, submission.getProblem().getCode(), submission.getUser().getHandle())
        );
    }

    @Override
    public Submission save(Submission submission) {
        return submissionRepo.save(submission);
    }

    @Override
    public boolean updateSubmissionOpen(Long submissionId , Authentication authentication) {
        Submission submission = submissionRepo.findById(submissionId)
                .orElseThrow(() -> new XJudgeException("Submission not found." , SubmissionServiceImpl.class.getName() , HttpStatus.NOT_FOUND));
        if(!isUserSubmission(submission.getUser() , authentication.getName())){
            throw new XJudgeException("Un Authenticated User" , SubmissionServiceImpl.class.getName() , HttpStatus.FORBIDDEN);
        }
        submission.setIsOpen(!submission.getIsOpen());
        submissionRepo.save(submission);
        return submission.getIsOpen();
    }

    @Override
    public List<Submission> getSubmissionsByContestId(Long contestId) {
        return submissionRepo.findAllByContestId(contestId);
    }

    @Override
    public List<Submission> getSubmissionsByContestIdAndUserId(Long contestId, Long userId) {
        return submissionRepo.findSubmissionsByContestIdAndUserId(contestId , userId);
    }

    @Override
    public Integer getSolvedCount(String problemCode, OnlineJudgeType onlineJudgeType) {
        return submissionRepo.getSolvedCount(problemCode, onlineJudgeType);
    }

    @Override
    public Page<SubmissionPageModel> filterSubmissions(String userHandle, String oj, String problemCode, String language, Pageable pageable) {
        Page<Submission> submissions = submissionRepo.filterSubmissions(userHandle, oj, problemCode, language, pageable);
        return submissions.map(submission ->
                submissionMapper.toPageModel(submission, submission.getProblem().getCode(), submission.getUser().getHandle())
        );
    }

    @Override
    public Page<ContestStatusPageModel> filterSubmissionsInContest(Long contestId, String userHandle , String problemCode,String result ,String language, Pageable pageable) {
        Page<Submission> submissions = submissionRepo.filterContestSubmissions(contestId , userHandle, problemCode, result ,language, pageable);
        return submissions.map(submission ->
                submissionMapper
                        .toContestStatusPageModel(
                               submission , getProblemIndex(submission.getContest().getId() , submission.getProblem().getId())
                               )
        );
    }

    @Override
    public List<Submission> findByUserAndProblem(User user, Problem problem) {
        return submissionRepo.findByUserAndProblem(user, problem);
    }

    @Override
    public List<Submission> getSubmissionByStatus(String status) {
        return submissionRepo.findSubmissionsBySubmissionStatus(status);
    }

    private String getProblemIndex(Long contestId , Long problemId) {
        return  contestProblemService
                .getContestProblemByContestIdAndProblemId(contestId  , problemId)
                .getProblemHashtag();
    }

    private boolean isUserSubmission(User user , String loginUserHandle){
        return user.getHandle()
                .equals(loginUserHandle);
    }

    private void updateSubmissionStatus(Submission storedSubmission , Submission updatedSubmission){
        storedSubmission.setSubmissionStatus(updatedSubmission.getSubmissionStatus());
        storedSubmission.setMemoryUsage(updatedSubmission.getMemoryUsage());
        storedSubmission.setVerdict(updatedSubmission.getVerdict());
        storedSubmission.setTimeUsage(updatedSubmission.getTimeUsage());
        storedSubmission.setRemoteRunId(updatedSubmission.getRemoteRunId());
    }

    private SubmissionInfoModel getSubmissionInfo(Submission submission){
        Problem problem = submission.getProblem();
        return SubmissionInfoModel.builder()
                .code(problem.getCode())
                .ojType(problem.getOnlineJudge())
                .solutionCode(submission.getSolution())
                .isOpen(submission.getIsOpen())
                .compiler(submission.getCompiler())
                .build();
    }

    private SubmissionModel determineSubmissionModel(Submission submission , Authentication authentication){
        if ((authentication != null && isUserSubmission(submission.getUser(), authentication.getName()))
                || submission.getIsOpen())
            return submissionMapper.toOpenSubmissionModel(submission, submission.getSolution());
        return submissionMapper.toModel(submission);
    }
}
