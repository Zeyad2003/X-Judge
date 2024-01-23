package com.xjudge.model.submission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionInfo {

    @NotNull
    @NotBlank
    String solutionCode;

    @NotNull
    @PositiveOrZero
    Integer compilerId;
}
