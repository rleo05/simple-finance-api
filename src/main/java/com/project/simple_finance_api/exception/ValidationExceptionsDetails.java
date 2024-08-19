package com.project.simple_finance_api.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class ValidationExceptionsDetails extends StandardExceptionDetails{
    private String fields;
    private String fieldsMessage;
}
