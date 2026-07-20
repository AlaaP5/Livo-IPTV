package com.rand.ishowApi.account.mobile.application.service;



import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.account.domain.entity.AccountProfile;
import com.rand.ishowApi.account.domain.entity.AccountStatus;
import com.rand.ishowApi.account.domain.repository.AccountProfileRepository;
import com.rand.ishowApi.account.domain.repository.AccountRepository;
import com.rand.ishowApi.account.mobile.api.request.AccountLoginMobileRequest;
import com.rand.ishowApi.account.mobile.api.request.AccountRegisterMobileRequest;
import com.rand.ishowApi.account.mobile.api.request.AccountRestPasswordRequest;
import com.rand.ishowApi.account.mobile.api.request.AccountVerifyRequest;
import com.rand.ishowApi.account.mobile.api.response.AccountMobileResponse;
import com.rand.ishowApi.account.mobile.api.response.ProfileMobileResponse;
import com.rand.ishowApi.account.mobile.application.manager.AccountDomainManager;
import com.rand.ishowApi.account.mobile.application.manager.AccountProfileDomainManager;
import com.rand.ishowApi.account.mobile.mapper.AccountMobileMapper;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.keycloak.response.TokenResponse;
import com.rand.ishowApi.keycloak.service.KeyClockService;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.security.context.DeviceInfo;
import com.rand.ishowApi.security.context.UserContext;
import com.rand.ishowApi.shared.operator.Operator;
import com.rand.ishowApi.utils.OperatorUtils;
import com.rand.ishowApi.utils.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountMobileService {
    private final AccountDomainManager accountDomainManager;
    private final AccountProfileDomainManager accountProfileDomainManager;
    private final AccountRepository accountRepository;
    private final AccountProfileRepository accountProfileRepository;

    private final KeyClockService keyClockService;
    private final AccountMobileMapper mapper;

    public AccountMobileResponse login(AccountLoginMobileRequest request) {
        validateLoginRequest(request);

        Account account = getAccountByGsm(request.gsm());
        validateAccountAccessible(account);
        validateAccountIsActive(account);

        TokenResponse tokenResponse = keyClockService.login(request.gsm(), request.password());
        AccountProfile profile = getProfileByAccount(account);

        accountDomainManager.setLoginMetadata(account, profile, request.fcmToken());
        accountRepository.save(account);
        accountProfileRepository.save(profile);

        return mapper.toResponse(tokenResponse, account, profile);
    }

    public ProfileMobileResponse editProfile(com.rand.ishowApi.account.mobile.api.request.AccountEditProfileRequest request) {
        validateEditProfileRequest(request);

        Account account = getCurrentAccount();
        validateAccountAccessible(account);

        AccountProfile profile = getProfileByAccount(account);
        accountProfileDomainManager.update(profile, request, account, getCurrentDeviceInfo());
        accountProfileRepository.save(profile);

        return mapper.toProfileResponse(profile, account);
    }



    public void register(AccountRegisterMobileRequest request) {
        validateRegisterRequest(request);
        validateGsmUniqueness(request.gsm());

        Operator operator = OperatorUtils.getGsmOperator(request.gsm());

        Account account = accountDomainManager.createMobileAccount(
                request,
                keyClockService.registerUser(mapper.fromMobile(request, operator)),
                operator,
                OtpGenerator.generateOtp()
        );
        account = accountRepository.save(account);

        AccountProfile profile = accountProfileDomainManager.create(request, account, getCurrentDeviceInfo());
        accountProfileRepository.save(profile);

        // TODO: send OTP through SMS provider integration.
    }

    public void restPassword(AccountRestPasswordRequest request) {
        validateResetPasswordRequest(request);

        Account account = getAccountByGsm(request.gsm());
        validateAccountAccessible(account);
        validateOtp(account, request.otp());

        keyClockService.resetPassword(account.getUserId(), request.password());
    }


    public void changePassword(com.rand.ishowApi.account.mobile.api.request.AccountChangePasswordRequest request) {
        validateChangePasswordRequest(request);

        Account account = getAccountByGsm(request.gsm());
        validateAccountAccessible(account);
        validateAccountIsActive(account);

        // Reuse Keycloak login to validate current password before reset.
        keyClockService.login(request.gsm(), request.oldPassword());

        keyClockService.resetPassword(account.getUserId(), request.password());
    }


    public AccountMobileResponse verify(AccountVerifyRequest request) {
        validateVerifyRequest(request);

        Account account = getAccountByGsm(request.gsm());
        validateAccountAccessible(account);
        validateOtp(account, request.otp());

        accountDomainManager.verifyAccount(account);
        accountDomainManager.setOtp(account, null);
        accountRepository.save(account);

        TokenResponse tokenResponse = keyClockService.login(request.gsm(), request.password());
        AccountProfile profile = getProfileByAccount(account);

        return mapper.toResponse(tokenResponse, account, profile);
    }

    public void sendOtp(String gsm) {
        validateRequired(gsm);

        Account account = getAccountByGsm(gsm);
        validateAccountAccessible(account);

        accountDomainManager.setOtp(account, OtpGenerator.generateOtp());
        accountRepository.save(account);
        // TODO: send OTP through SMS provider integration.
    }

    public void logout(String refreshToken) {
        validateRequired(refreshToken);
        keyClockService.logout(refreshToken);
    }

    public void delete() {
        Account account = getCurrentAccount();
        validateAccountAccessible(account);

        keyClockService.deleteKeycloakUser(account.getUserId());

        accountDomainManager.deleteAccount(account);
        accountRepository.save(account);
    }



    public TokenResponse refresh(String refreshToken) {
        validateRequired(refreshToken);
        return keyClockService.refresh(refreshToken);
    }

    // =================================================
    // ================ Validation =====================

    private Account getAccountByGsm(String gsm) {
        return accountRepository.findByGsmAndStatusNot(gsm, AccountStatus.DELETED)
                .orElseThrow(() -> new CustomException(ApiResponseCode.ACCOUNT_NOT_FOUND, null));
    }

    public Account getCurrentAccount() {
        String userId = UserContext.getUserId();
        if (userId == null || userId.isBlank()) {
            throw new CustomException(ApiResponseCode.ERROR_401, null);
        }

        return accountRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.ACCOUNT_NOT_FOUND, null));
    }

    private AccountProfile getProfileByAccount(Account account) {
        return accountProfileRepository.findByAccount(account)
                .orElseThrow(() -> new CustomException(ApiResponseCode.ACCOUNT_NOT_FOUND, null));
    }

    private DeviceInfo getCurrentDeviceInfo() {
        return DeviceContext.exists() ? DeviceContext.get() : null;
    }

    private void validateAccountAccessible(Account account) {
        if (account.getStatus() == AccountStatus.DELETED) {
            throw new CustomException(ApiResponseCode.ACCOUNT_NOT_FOUND, null);
        }

        if (account.getStatus() == AccountStatus.BANNED) {
            throw new CustomException(ApiResponseCode.ACCOUNT_BANNED, null);
        }
    }

    private void validateAccountIsActive(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new CustomException(ApiResponseCode.ACCOUNT_VERIFICATION_REQUIRED, null);
        }
    }

    private void validateOtp(Account account, String otp) {
        if (otp == null || otp.isBlank() || account.getOtp() == null || !account.getOtp().equals(otp)) {
            throw new CustomException(ApiResponseCode.ACCOUNT_OTP_INVALID, null);
        }
    }

    private void validateGsmUniqueness(String gsm) {
        if (accountRepository.existsByGsmAndStatusNot(gsm, AccountStatus.DELETED)) {
            throw new CustomException(ApiResponseCode.ACCOUNT_GSM_EXISTS, null);
        }
    }

    private void validateLoginRequest(AccountLoginMobileRequest request) {
        if (request == null) {
            throw new CustomException(ApiResponseCode.ERROR_400, null);
        }
        validateRequired(request.gsm());
        validateRequired(request.password());
    }

    private void validateRegisterRequest(AccountRegisterMobileRequest request) {
        if (request == null) {
            throw new CustomException(ApiResponseCode.ERROR_400, null);
        }
        validateRequired(request.gsm());
        validateRequired(request.password());
        validateRequired(request.fullName());
        if (request.gender() == null || request.birthdate() == null) {
            throw new CustomException(ApiResponseCode.ERROR_400, null);
        }
    }

    private void validateResetPasswordRequest(AccountRestPasswordRequest request) {
        if (request == null) {
            throw new CustomException(ApiResponseCode.ERROR_400, null);
        }
        validateRequired(request.gsm());
        validateRequired(request.password());
        validateRequired(request.otp());
    }

    private void validateChangePasswordRequest(com.rand.ishowApi.account.mobile.api.request.AccountChangePasswordRequest request) {
        if (request == null) {
            throw new CustomException(ApiResponseCode.ERROR_400, null);
        }
        validateRequired(request.gsm());
        validateRequired(request.oldPassword());
        validateRequired(request.password());
    }

    private void validateVerifyRequest(AccountVerifyRequest request) {
        if (request == null) {
            throw new CustomException(ApiResponseCode.ERROR_400, null);
        }
        validateRequired(request.gsm());
        validateRequired(request.otp());
    }

    private void validateEditProfileRequest(com.rand.ishowApi.account.mobile.api.request.AccountEditProfileRequest request) {
        if (request == null) {
            throw new CustomException(ApiResponseCode.ERROR_400, null);
        }
        validateRequired(request.fullName());
        if (request.gender() == null || request.birthdate() == null) {
            throw new CustomException(ApiResponseCode.ERROR_400, null);
        }
    }

    private void validateRequired(String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(ApiResponseCode.ERROR_400, null);
        }
    }


}
