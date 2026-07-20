package com.rand.ishowApi.keycloak.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rand.ishowApi.account.domain.entity.AccountRole;
import lombok.Data;

@Data
public class TokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("refresh_expires_in")
    private Long refreshExpiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    private AccountRole role;


}
