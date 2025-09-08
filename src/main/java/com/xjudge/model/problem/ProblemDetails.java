package com.xjudge.model.problem;

import com.xjudge.model.enums.OnlineJudgeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDetails {
    private String code; // e.g., 2134C
    private OnlineJudgeType onlineJudge;
    private String title;
    private String contestName;
    private String problemUrl;
    private String contestUrl;

    private Map<String, Object> sections;
    private Map<String, Object> samples;
    private String constraints;
}
