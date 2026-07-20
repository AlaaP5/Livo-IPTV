package com.rand.ishowApi.account.mobile.application.manager;


import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.account.domain.entity.AccountProfile;
import com.rand.ishowApi.account.domain.entity.AccountRole;
import com.rand.ishowApi.account.domain.entity.AccountStatus;
import com.rand.ishowApi.account.mobile.api.request.AccountRegisterMobileRequest;
import com.rand.ishowApi.shared.operator.Operator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AccountDomainManager {

    public Account createMobileAccount(AccountRegisterMobileRequest request, String userId, Operator operator, String otp) {
        Account account = new Account();
        account.setUserId(userId);
        account.setGsm(request.gsm());
        account.setUsername(request.gsm());
        account.setRole(AccountRole.USER);
        account.setOperator(operator);
        account.setOtp(otp);
        account.setStatus(AccountStatus.UNVERIFIED);
        return account;
    }

    public void verifyAccount(Account account) {
        account.setStatus(AccountStatus.ACTIVE);
    }

    public void deleteAccount(Account account) {
        account.setStatus(AccountStatus.DELETED);
    }

    public void setLoginMetadata(Account account, AccountProfile profile, String fcmToken) {
        profile.setFcmToken(fcmToken);
        account.setLastLogin(LocalDateTime.now());
    }

    public void setOtp(Account account, String otp) {
        account.setOtp(otp);
    }
}
