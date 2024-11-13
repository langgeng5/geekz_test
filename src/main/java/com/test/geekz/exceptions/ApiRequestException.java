package com.test.geekz.exceptions;

import javax.management.RuntimeErrorException;

public class ApiRequestException extends RuntimeErrorException {

    private String customMessage;
    
    public ApiRequestException(String customMessage, Error arg0) {
        super(arg0);
        this.customMessage = customMessage;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }
}
