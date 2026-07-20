package com.rand.ishowApi.account.mobile.api.response;


import com.rand.ishowApi.shared.gender.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountMobileResponse {

    private Long accountId;
    private String userId;
    private String gsm;
    private String fullName;
    private String accessToken;
    private String refreshToken;
    private String photoPath;
    private String email;
    private String operator;
    private LocalDate birthdate;
    private Gender gender;
}
