package com.etna.myapi.utils;

public final class Constants {

    private Constants() {
    }

    public static final String regexMail = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String password = "^{6,35}$";
    public static final String username = "^[a-zA-Z0-9_-]{4,35}$";
}
