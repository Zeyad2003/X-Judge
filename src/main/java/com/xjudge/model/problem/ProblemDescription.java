package com.xjudge.model.problem;

import com.xjudge.entity.Section;
import lombok.Builder;

import java.util.List;

@Builder
public record ProblemDescription(
        String prependHtml,
        List<Section> sections
) {
}
