package com.xjudge.service.problemscraping;

import com.xjudge.model.submission.SubmissionInfo;
import com.xjudge.model.submission.SubmissionResult;

public interface SubmissionAutomation {
    void login();
    SubmissionResult submit(String problemCode, SubmissionInfo data);
}
