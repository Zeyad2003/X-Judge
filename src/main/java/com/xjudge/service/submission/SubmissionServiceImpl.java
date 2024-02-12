package com.xjudge.service.submission;

import com.xjudge.entity.Submission;
import com.xjudge.repository.SubmissionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
