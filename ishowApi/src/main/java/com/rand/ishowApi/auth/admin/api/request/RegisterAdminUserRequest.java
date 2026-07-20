package com.rand.ishowApi.auth.admin.api.request;

import com.rand.ishowApi.account.domain.entity.AccountRole;
import lombok.Data;

@Data
public class RegisterAdminUserRequest {
    private String username;
    private String password;
    private String email;
    private String name;
    private String gsm;
    private String operator;
    private AccountRole role;
}
