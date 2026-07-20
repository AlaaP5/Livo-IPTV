package com.rand.ishowApi.keycloak.request;

import com.rand.ishowApi.account.domain.entity.AccountRole;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String password;
    private String gsm = "0993333614";
    private String operator = "rand";
    private AccountRole role;
}
