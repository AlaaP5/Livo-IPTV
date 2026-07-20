package com.rand.ishowApi.account.admin.mapper;

import com.rand.ishowApi.account.admin.api.request.AccountAdminRequest;
import com.rand.ishowApi.account.admin.api.response.AccountAdminResponse;
import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.keycloak.request.CreateUserRequest;
import org.springframework.stereotype.Component;

@Component
public class AccountAdminMapper {

    public CreateUserRequest fromAdmin(AccountAdminRequest request) {
        CreateUserRequest model = new CreateUserRequest();
        model.setUsername(request.username());
        model.setFirstName(request.username());
        model.setLastName(request.username());
        model.setEmail(request.username() + "@iShow.sy");
        model.setPassword(request.password());
        model.setRole(request.role());
        return model;
    }

    public AccountAdminResponse toResponse (Account account){

        AccountAdminResponse response = new AccountAdminResponse();

        response.setId(account.getId());
        response.setUserName(account.getUsername());
        response.setUserId(account.getUserId());
        response.setAccountStatus(account.getStatus());

        return response;
    }
}
