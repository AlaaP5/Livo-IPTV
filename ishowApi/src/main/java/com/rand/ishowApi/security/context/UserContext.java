package com.rand.ishowApi.security.context;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class UserContext {
    private static final ThreadLocal<Map<String, Object>> userClaimsHolder = new ThreadLocal<>();
    private static final ThreadLocal<Set<String>> userRolesHolder = new ThreadLocal<>();

    // Set all custom claims
    public static void setUserClaims(Map<String, Object> claims) {
        userClaimsHolder.set(claims);
    }

    // Get all custom claims
    public static Map<String, Object> getUserClaims() {
        return userClaimsHolder.get();
    }

    // Convenience getters for common claims
    public static String getUserId() {
        Map<String, Object> claims = getUserClaims();
        return claims != null ? (String) claims.get("sub") : null;
    }

    public static String getSid() {
        Map<String, Object> claims = getUserClaims();
        return claims != null ? (String) claims.get("sid") : null;
    }

    public static String getGsm() {
        Map<String, Object> claims = getUserClaims();
        return claims != null ? (String) claims.get("gsm") : null;
    }

    public static String getEmail() {
        Map<String, Object> claims = getUserClaims();
        return claims != null ? (String) claims.get("email") : null;
    }

    public static String getUsername() {
        Map<String, Object> claims = getUserClaims();
        return claims != null ? (String) claims.get("preferred_username") : null;
    }

    public static String getOperator() {
        Map<String, Object> claims = getUserClaims();
        return claims != null ? (String) claims.get("operator") : null;
    }

    public static String getAccountId() {
        Map<String, Object> claims = getUserClaims();
        return claims != null ? (String) claims.get("account_id") : null;
    }


    public static Boolean isEmailVerified() {
        Map<String, Object> claims = getUserClaims();
        return claims != null ? (Boolean) claims.get("email_verified") : null;
    }

    // ================= ROLES =================
    public static void setRoles(Set<String> roles) {
        userRolesHolder.set(roles);
    }

    public static Set<String> getRoles() {
        Set<String> roles = userRolesHolder.get();
        return roles != null ? roles : Collections.emptySet();
    }

    public static boolean hasRole(String role) {
        return getRoles().contains("ROLE_" + role);
    }


    // Clear context
    public static void clear() {
        userClaimsHolder.remove();
        userRolesHolder.remove();
    }
}
