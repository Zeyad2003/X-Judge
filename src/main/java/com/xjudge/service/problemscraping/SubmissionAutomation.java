package com.xjudge.service.problemscraping;

import com.xjudge.model.SubmissionInfo;
import com.xjudge.model.SubmissionResult;

public interface SubmissionAutomation {
    void login();
    SubmissionResult submit(String problemCode, SubmissionInfo data);
}
