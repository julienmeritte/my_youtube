package com.etna.myapi.Exception;

public class CustomInvalidPagerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    final int page;
    final int perPage;
    final String message;

    public CustomInvalidPagerException(int page, int perPage, String message) {
        this.page = page;
        this.perPage = perPage;
        this.message = message;
    }

    public int getPage() {
        return page;
    }

    public int getPerPage() {
        return perPage;
    }

    public String getMessage() {
        return message;
    }
}