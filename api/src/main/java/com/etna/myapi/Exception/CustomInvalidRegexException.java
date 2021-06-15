package com.etna.myapi.Exception;

public class CustomInvalidRegexException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    final String type;
    final String variable;

    public CustomInvalidRegexException(String variable, String type) {
        this.type = type;
        this.variable = variable;
    }

    public String getType() {
        return type;
    }

    public String getVariable() {
        return variable;
    }
}