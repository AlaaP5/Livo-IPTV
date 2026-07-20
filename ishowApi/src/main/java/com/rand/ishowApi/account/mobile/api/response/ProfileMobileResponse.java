package com.rand.ishowApi.account.mobile.api.response;


import lombok.Data;

@Data
public class ProfileMobileResponse {

    private Long accountId;
    private String userId;
    private String gsm;
    private String fullName;
    private String photoPath;
    private String email;
    private String operator;

}
