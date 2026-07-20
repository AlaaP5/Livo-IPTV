package com.rand.ishowApi.auth.admin.api.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

