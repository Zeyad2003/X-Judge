package com.xjudge.model.contest;

import com.xjudge.model.enums.ContestVisibility;

import java.time.Duration;
import java.time.Instant;

public record ContestPageModel(
        Long id ,

        String title ,

        Long numberOfParticipants ,

        Instant beginTime ,

        Duration duration ,

        Long ownerId ,

        String handle ,

        String photoUrl,

        Long groupId ,

        ContestVisibility visibility ,

        String groupName
) {
}