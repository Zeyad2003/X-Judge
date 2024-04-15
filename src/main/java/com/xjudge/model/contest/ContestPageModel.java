package com.xjudge.model.contest;

import java.time.Duration;
import java.time.Instant;

public record ContestPageModel(
        Long id ,
        String title ,
        Long numberOfParticipants ,
        Instant beginTime ,
        Duration duration ,
        UserContestPageModel owner
) {
}