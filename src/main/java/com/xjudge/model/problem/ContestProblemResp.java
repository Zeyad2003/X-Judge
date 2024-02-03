package com.xjudge.model.problem;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContestProblemResp {
    private Long problemId;
    private String problemCode;
    private String title;
    private String platform;
}
