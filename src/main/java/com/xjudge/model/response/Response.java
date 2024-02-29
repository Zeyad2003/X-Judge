package com.xjudge.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response <T> {
    private int code;
    private boolean success;
    private T data;
    private String message;
    private String error;
}
