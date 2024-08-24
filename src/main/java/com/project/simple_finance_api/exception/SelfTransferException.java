package com.project.simple_finance_api.exception;

public class SelfTransferException extends RuntimeException{
    public SelfTransferException(String message){
        super(message);
    }

}
