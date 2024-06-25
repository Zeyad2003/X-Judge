package com.xjudge.model.scrap;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class SubmissionScrapedData {
    String time;
    String memory;
    String verdict;
    String remoteId;
}

