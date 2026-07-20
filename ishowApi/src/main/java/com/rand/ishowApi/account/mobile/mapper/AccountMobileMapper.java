package com.rand.ishowApi.account.mobile.mapper;



import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.account.domain.entity.AccountProfile;
import com.rand.ishowApi.account.domain.entity.AccountRole;
import com.rand.ishowApi.account.mobile.api.request.AccountRegisterMobileRequest;
import com.rand.ishowApi.account.mobile.api.response.AccountMobileResponse;
import com.rand.ishowApi.account.mobile.api.response.ProfileMobileResponse;
import com.rand.ishowApi.keycloak.request.CreateUserRequest;
import com.rand.ishowApi.keycloak.response.TokenResponse;
import com.rand.ishowApi.shared.operator.Operator;
import org.springframework.stereotype.Component;

@Component
public class AccountMobileMapper {


    public CreateUserRequest fromMobile(AccountRegisterMobileRequest request, Operator operator) {
        CreateUserRequest model = new CreateUserRequest();
        model.setUsername(request.gsm());
        model.setPassword(request.password());
        model.setRole(AccountRole.USER);
        model.setGsm(request.gsm());
        model.setEmail("user-"+request.gsm()+"@ishow.sy");
        model.setOperator(operator.name());
        model.setFirstName(request.fullName());
        model.setLastName(request.fullName());

        return model;
    }

    public ProfileMobileResponse toProfileResponse(AccountProfile profile, Account account) {
        ProfileMobileResponse profileMobileResponse = new ProfileMobileResponse();

        profileMobileResponse.setAccountId(account.getId());
        profileMobileResponse.setEmail(account.getEmail());
        profileMobileResponse.setGsm(account.getGsm());
        profileMobileResponse.setOperator(account.getOperator() != null ? account.getOperator().name() : null);
        profileMobileResponse.setFullName(profile.getFullName());
        profileMobileResponse.setPhotoPath(profile.getPhotoPath());
        profileMobileResponse.setUserId(account.getUserId());

        return profileMobileResponse;

    }


    public AccountMobileResponse toResponse(TokenResponse tokenResponse, Account account, AccountProfile profile) {
        AccountMobileResponse accountResponse = new AccountMobileResponse();
        accountResponse.setAccountId(account.getId());
        accountResponse.setUserId(account.getUserId());
        accountResponse.setGsm(account.getGsm());
        accountResponse.setFullName(profile.getFullName());
        accountResponse.setAccessToken(tokenResponse != null ? tokenResponse.getAccessToken() : null);
        accountResponse.setRefreshToken(tokenResponse != null ? tokenResponse.getRefreshToken() : null);
        accountResponse.setPhotoPath(profile.getPhotoPath());
        accountResponse.setEmail(account.getEmail());
        accountResponse.setOperator(account.getOperator() != null ? account.getOperator().name() : null);
        accountResponse.setBirthdate(profile.getBirthdate());
        accountResponse.setGender(profile.getGender());


        return accountResponse;
    }


}



