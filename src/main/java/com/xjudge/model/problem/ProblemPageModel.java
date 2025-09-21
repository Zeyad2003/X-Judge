package com.xjudge.model.problem;

import java.time.Instant;

import com.xjudge.model.enums.OnlineJudgeType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemPageModel {

    @NotNull(message = "Online judge type must not be null")
    private OnlineJudgeType onlineJudge;

    @NotBlank(message = "Problem code must not be blank")
    @Size(max = 100, message = "Problem code must not exceed 100 characters")
    private String code;

    @NotBlank(message = "Title must not be blank")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 512, message = "Problem URL must not exceed 512 characters")
    private String problemUrl;

    @Size(max = 255, message = "Contest name must not exceed 255 characters")
    private String contestName;

    @Size(max = 512, message = "Contest URL must not exceed 512 characters")
    private String contestUrl;

    @PositiveOrZero(message = "Solved count must be zero or positive")
    private Integer solvedCount;

    private Instant lastModifiedDate;
}
