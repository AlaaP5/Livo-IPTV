package com.rand.ishowApi.account.admin.api.response;

import com.rand.ishowApi.account.domain.entity.AccountStatus;
import lombok.Data;

@Data
public class AccountAdminResponse {

    private Long id;
    private String userId;
    private String userName;
    private AccountStatus accountStatus;
}
