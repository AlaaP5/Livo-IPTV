package com.rand.ishowApi.account.admin.api.request;

import com.rand.ishowApi.account.domain.entity.AccountRole;
import com.rand.ishowApi.account.domain.entity.AccountStatus;
import com.rand.ishowApi.common.PaginationFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountAdminFilterRequest extends PaginationFilter {

    private String userName;
    private AccountRole role;
    private AccountStatus status;
}
