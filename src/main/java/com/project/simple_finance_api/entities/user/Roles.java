package com.project.simple_finance_api.entities.user;

public enum Roles {
    ADMIN("ADMIN"),
    USER("USER");

    final String code;

    Roles(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
