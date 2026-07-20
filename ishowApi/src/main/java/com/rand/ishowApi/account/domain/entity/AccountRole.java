package com.rand.ishowApi.account.domain.entity;


import lombok.Getter;

import java.util.Set;

@Getter
public enum AccountRole {

    USER("USER", "مستخدم"),

    CONTENT("CONTENT", "محتوى"),
    ADMIN("ADMIN", "مدير"),
    SUPER_ADMIN("SUPER_ADMIN", "مدير نظام"),

    MTN_MARKETING("MTN_MARKETING", "تسويق MTN"),
    MTN_CS("MTN_CS", "خدمة عملاء MTN");

    private final String code;
    private final String labelAr;

    AccountRole(String code, String labelAr) {
        this.code = code;
        this.labelAr = labelAr;
    }


    public static AccountRole fromAuthorities(Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return null;
        }

        for (String role : roles) {

            String cleaned = role.startsWith("ROLE_")
                    ? role.substring(5)
                    : role;

            try {
                return AccountRole.valueOf(cleaned);
            } catch (IllegalArgumentException ignored) {
            }
        }

        return null;
    }
}