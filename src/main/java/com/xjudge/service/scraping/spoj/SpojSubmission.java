package com.xjudge.service.scraping.spoj;

import com.xjudge.entity.Submission;
import com.xjudge.model.submission.SubmissionInfoModel;
import com.xjudge.service.scraping.strategy.SubmissionStrategy;
import org.springframework.stereotype.Service;

@Service
public class SpojSubmission implements SubmissionStrategy {
    @Override
    public Submission submit(SubmissionInfoModel data) {
        return null;
    }
}
