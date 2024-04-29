package com.xjudge.service.submission;

import com.xjudge.entity.Submission;
import com.xjudge.mapper.SubmissionMapper;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.repository.SubmissionRepo;
import lombok.RequiredArgsConstructor;
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
}
