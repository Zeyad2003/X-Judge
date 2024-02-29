package com.xjudge.service.submission;

import com.xjudge.entity.Submission;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.repository.SubmissionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepo submissionRepo;

    @Override
    public Submission getSubmissionById(Long submissionId) {
        return submissionRepo.findById(submissionId).orElseThrow(() -> new RuntimeException("Submission not found."));
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
    public Integer getSolvedCount(String problemCode, OnlineJudgeType onlineJudgeType) {
        return submissionRepo.getSolvedCount(problemCode, onlineJudgeType);
    }
}
