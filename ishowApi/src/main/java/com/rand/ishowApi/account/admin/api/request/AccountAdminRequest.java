package com.rand.ishowApi.account.admin.api.request;

import com.rand.ishowApi.account.domain.entity.AccountRole;
import com.rand.ishowApi.account.domain.entity.AccountStatus;

public record AccountAdminRequest(Long id, String username, String password, AccountRole role, AccountStatus status) {}

