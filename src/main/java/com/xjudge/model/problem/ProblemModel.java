package com.xjudge.model.problem;

import com.xjudge.entity.Compiler;
import com.xjudge.model.enums.OnlineJudgeType;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Map;

public record ProblemModel(
    @NotNull Long id,
    @NotBlank String problemCode,
    @NotBlank String problemTitle,
    @NotBlank String problemStatement,
    @NotBlank String problemInput,
    @NotBlank String problemOutput,
    @NotNull OnlineJudgeType problemSource,
    @NotBlank String problemTimeLimit,
    @NotBlank String problemMemoryLimit,
    @NotEmpty List<Compiler> problemCompilers,
    @NotNull Map<String, Object> extraInfo
) {}