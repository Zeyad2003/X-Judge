package com.xjudge.model.problem;

import com.xjudge.model.enums.FetchingStatus;
import com.xjudge.model.enums.OnlineJudgeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDetails {
    private Long id;

    private String code; // e.g., 2134C

    private OnlineJudgeType onlineJudge;

    private FetchingStatus fetchingStatus;

    private String title;

    private String contestName;

    private String problemUrl;

    private String contestUrl;

    private Integer solvedCount;

    @Builder.Default
    private List<SampleTestCaseDto> sampleTestCases = new ArrayList<>();

    @Builder.Default
    private List<SectionDto> sections = new ArrayList<>();

    @Builder.Default
    private List<PropertyDto> properties = new ArrayList<>();

    private Map<String, Object> extraMetadata;
}
