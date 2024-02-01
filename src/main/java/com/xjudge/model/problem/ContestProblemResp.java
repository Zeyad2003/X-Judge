package com.xjudge.model.problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class ContestProblemResp {
    private Long problemId;
    private String problemCode;
    private String title;
    private String platform;
}
