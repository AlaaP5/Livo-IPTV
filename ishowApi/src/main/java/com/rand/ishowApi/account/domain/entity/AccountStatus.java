package com.rand.ishowApi.account.domain.entity;

public enum AccountStatus {
    UNVERIFIED("unverified"),
    ACTIVE("active"),
    DELETED("deleted"),
    BANNED("banned");

    private final String value;

    AccountStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
