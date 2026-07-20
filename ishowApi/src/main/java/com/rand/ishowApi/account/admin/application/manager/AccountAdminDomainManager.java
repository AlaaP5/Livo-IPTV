package com.rand.ishowApi.account.admin.application.manager;

import com.rand.ishowApi.account.admin.api.request.AccountAdminRequest;
import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.account.domain.entity.AccountStatus;
import org.springframework.stereotype.Component;

@Component
public class AccountAdminDomainManager {

    public Account createAdminAccount(AccountAdminRequest request, String userId){
        Account account = new Account();

        account.setUsername(request.username());
        account.setUserId(userId);
        account.setRole(request.role());
        account.setStatus(request.status());

        return account;
    }

    public void updateAdminAccount(Account account, AccountAdminRequest request){
        if (request.username() != null) account.setUsername(request.username());
        if (request.role() != null) account.setRole(request.role());
        if (request.status() != null) account.setStatus(request.status());
    }

    public void setActiveAdminAccount(Account account, AccountStatus status){
        account.setStatus(status);
    }
}
