package com.xjudge.model.contest;

import com.xjudge.entity.Problem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContestProblemData {

    @NotNull
    @Min(value = 1 , message = "PROBLEM ID SHOULD BE GREATER THAN 0")
    private Long problemId;

    @NotNull
    @Min(value = 1)
    private int problemWeight;

    private String alias;

    private String code;
}
