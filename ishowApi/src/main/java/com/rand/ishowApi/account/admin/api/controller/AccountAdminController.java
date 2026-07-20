package com.rand.ishowApi.account.admin.api.controller;

import com.rand.ishowApi.account.admin.api.request.AccountAdminFilterRequest;
import com.rand.ishowApi.account.admin.api.request.AccountAdminRequest;
import com.rand.ishowApi.account.admin.api.request.ChangePasswordRequest;
import com.rand.ishowApi.account.admin.api.response.AccountAdminResponse;
import com.rand.ishowApi.account.admin.application.service.AccountAdminService;
import com.rand.ishowApi.account.domain.entity.AccountStatus;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/account")
@RequiredArgsConstructor
public class AccountAdminController {

    private final AccountAdminService service;

    @PostMapping("/create")
    public ApiResponse<AccountAdminResponse> create(
            @Valid @RequestBody AccountAdminRequest request) {
        return ApiResponse.success(service.createAccount(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("/update")
    public ApiResponse<AccountAdminResponse> update(
            @Valid @RequestBody AccountAdminRequest request) {
        return ApiResponse.success(service.updateAccount(request),ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{id}/status")
    public ApiResponse<AccountAdminResponse> changeAccountStatus(
            @PathVariable Long id,
            @RequestParam AccountStatus status) {
        return ApiResponse.success(service.activateAccount(id, status),ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("/change-password")
    public ApiResponse<AccountAdminResponse> changePassword(
            @RequestBody ChangePasswordRequest request) {
        return ApiResponse.success(service.changePassword(request),ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/list")
    public ApiResponse<List<AccountAdminResponse>> filterAccount(AccountAdminFilterRequest request) {

        Page<AccountAdminResponse> page = service.filterAccount(request);

        return ApiResponse.success(
                page.getContent(),
                ApiResponseCode.SUCCESS_200,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }
}
