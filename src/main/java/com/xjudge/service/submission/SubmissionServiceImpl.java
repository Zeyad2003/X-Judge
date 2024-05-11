package com.xjudge.service.submission;

import com.xjudge.entity.Submission;
import com.xjudge.mapper.SubmissionMapper;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.model.submission.SubmissionPageModel;
import com.xjudge.repository.SubmissionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepo submissionRepo;
    private final SubmissionMapper submissionMapper;

    @Override
    public SubmissionModel getSubmissionById(Long submissionId) {
        Submission submission = submissionRepo.findById(submissionId).orElseThrow(() -> new RuntimeException("Submission not found."));
        return submissionMapper.toModel(submission);
    }

    @Override
    public Page<SubmissionPageModel> getAllSubmissions(Pageable pageable) {
        Page<Submission> submissions = submissionRepo.findAll(pageable);
        return submissions.map(submission ->
                submissionMapper
                        .toPageModel(
                                submission,submission.getProblem().getCode(),
                                submission.getUser().getHandle()
                        )
        );
    }

    @Override
    public Submission save(Submission submission) {
        return submissionRepo.save(submission);
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
                submissionMapper
                        .toPageModel(
                                submission,
                                submission.getProblem().getCode(),
                                submission.getUser().getHandle()
                        )
        );
    }
}
