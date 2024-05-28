package com.xjudge.model.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupContestModel {
    private Long id;
    private String title;
    private Instant beginTime;
    private Duration duration;
}
