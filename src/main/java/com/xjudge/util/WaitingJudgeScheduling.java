package com.xjudge.util;

import com.xjudge.entity.Submission;
import com.xjudge.service.submission.SubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WaitingJudgeScheduling {
    private final long EVERY_24_INTERVAL = 86400000;
    private final long ONE_HOUR_DELAY=3600000;
    private final SubmissionService submissionService;

    @Autowired
    public WaitingJudgeScheduling(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }


    @Scheduled(fixedRate = EVERY_24_INTERVAL ,  initialDelay = ONE_HOUR_DELAY)
    public void judge() {
        List<Submission> unSubmitted = submissionService.getSubmissionByStatus("unsubmitted");
        unSubmitted
                .forEach(
                       submissionService::resubmit
                );
    }
}
