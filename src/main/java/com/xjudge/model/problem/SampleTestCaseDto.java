package com.xjudge.model.problem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SampleTestCaseDto {
    private Long id;

    private String input;

    private String output;

    private Integer sampleOrder;
}
