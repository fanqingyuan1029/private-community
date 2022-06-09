package com.fqy.crm.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidaException extends RuntimeException {

    private Map<String, String> exceptionMessages = new HashMap<>();

    public ValidaException() {
    }

    public ValidaException(String message) {
        super(message);
    }

    public Map<String, String> getExceptionMessages() {
        return exceptionMessages;
    }

    public void setExceptionMessages(Map<String, String> exceptionMessages) {
        this.exceptionMessages = exceptionMessages;
    }
}
