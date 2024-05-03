package com.xjudge.service.submission;

import com.xjudge.entity.Submission;
import com.xjudge.model.enums.OnlineJudgeType;
import com.xjudge.model.submission.SubmissionModel;
import com.xjudge.model.submission.SubmissionPageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubmissionService {
    SubmissionModel getSubmissionById(Long submissionId);

    Page<SubmissionPageModel> getAllSubmissions(Pageable pageable);

    Submission save(Submission submission);

    List<Submission> getSubmissionsByContestId(Long contestId);

    List<Submission> getSubmissionsByContestIdAndUserId(Long contestId , Long userId);

    Integer getSolvedCount(String problemCode, OnlineJudgeType onlineJudgeType);

    Page<SubmissionPageModel> filterSubmissions(String userHandle, String oj, String problemCode, String language, Pageable pageable);
}
