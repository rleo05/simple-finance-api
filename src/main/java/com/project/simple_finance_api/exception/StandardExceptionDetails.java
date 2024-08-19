package com.project.simple_finance_api.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Data
public class StandardExceptionDetails {
    private Integer status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private String path;
}
