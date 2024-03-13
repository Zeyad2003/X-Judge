package com.xjudge.service.scraping;

import com.xjudge.entity.Submission;
import com.xjudge.model.submission.SubmissionInfoModel;

public interface SubmissionAutomation {
    Submission submit(SubmissionInfoModel data);
}
