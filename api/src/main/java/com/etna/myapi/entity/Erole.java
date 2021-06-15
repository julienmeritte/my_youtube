package com.etna.myapi.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Erole implements GrantedAuthority {
    ADMIN, USER;

    public String getAuthority() {
        return name();
    }
}
