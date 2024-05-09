package com.xjudge.service.scraping.strategy;

import com.xjudge.entity.Submission;
import com.xjudge.model.submission.SubmissionInfoModel;

public interface SubmissionStrategy {
    Submission submit(SubmissionInfoModel data);
}
