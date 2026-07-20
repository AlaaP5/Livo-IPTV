package com.rand.ishowApi.account.mobile.application.manager;

import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.account.domain.entity.AccountProfile;
import com.rand.ishowApi.account.mobile.api.request.AccountEditProfileRequest;
import com.rand.ishowApi.account.mobile.api.request.AccountRegisterMobileRequest;
import com.rand.ishowApi.security.context.DeviceInfo;
import org.springframework.stereotype.Component;

@Component
public class AccountProfileDomainManager {

    public AccountProfile create(AccountRegisterMobileRequest request, Account account, DeviceInfo deviceInfo) {
        AccountProfile profile = new AccountProfile();
        profile.setFullName(request.fullName());
        profile.setGender(request.gender());
        profile.setBirthdate(request.birthdate());
        profile.setAccount(account);
        applyDeviceMetadata(profile, deviceInfo);
        return profile;
    }

    public void update(AccountProfile accountProfile, AccountEditProfileRequest request, Account account, DeviceInfo deviceInfo) {
        accountProfile.setFullName(request.fullName());
        accountProfile.setGender(request.gender());
        accountProfile.setBirthdate(request.birthdate());
        accountProfile.setAccount(account);
        applyDeviceMetadata(accountProfile, deviceInfo);
    }

    private void applyDeviceMetadata(AccountProfile profile, DeviceInfo deviceInfo) {
        if (deviceInfo == null) {
            return;
        }

        profile.setAppVersion(deviceInfo.getAppVersion());
        profile.setLanguage(deviceInfo.getLanguage());
        profile.setMobileModel(deviceInfo.getMobileModel());
        profile.setMobileManufacturer(deviceInfo.getMobileManufacturer());
        profile.setPlatform(deviceInfo.getPlatform());
    }
}
