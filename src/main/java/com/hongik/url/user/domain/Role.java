package com.hongik.url.user.domain;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }
}
