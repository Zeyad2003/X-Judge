package com.xjudge.service.scraping;

import com.xjudge.model.submission.SubmissionInfo;
import com.xjudge.model.submission.SubmissionResult;

public interface SubmissionAutomation {
    void login();
    SubmissionResult submit(SubmissionInfo data);
}
