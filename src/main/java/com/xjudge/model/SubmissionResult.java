package com.xjudge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionResult {
    String verdict;

    String time;

    String memory;

    String submitTime;
}
