package com.xjudge.model.problem;

import com.xjudge.model.enums.SectionFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionDto {
    private Long id;

    private String title;

    private SectionFormat sectionFormat;

    private String content;

    private Integer sectionOrder;
}
