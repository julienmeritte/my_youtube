package com.etna.myapi.Exception;

public class CustomFormatException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    final String message;
    final String format;
    final String file;

    public CustomFormatException(String message, String format, String file) {
        this.message = message;
        this.format = format;
        this.file = file;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getFormat() {
        return format;
    }

    public String getFile() {
        return file;
    }
}