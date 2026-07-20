package com.rand.ishowApi.account.admin.application.service;

import com.rand.ishowApi.account.admin.api.request.AccountAdminFilterRequest;
import com.rand.ishowApi.account.admin.api.request.AccountAdminRequest;
import com.rand.ishowApi.account.admin.api.request.ChangePasswordRequest;
import com.rand.ishowApi.account.admin.api.response.AccountAdminResponse;
import com.rand.ishowApi.account.admin.application.manager.AccountAdminDomainManager;
import com.rand.ishowApi.account.admin.mapper.AccountAdminMapper;
import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.account.domain.entity.AccountStatus;
import com.rand.ishowApi.account.domain.repository.AccountRepository;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.keycloak.service.KeyClockService;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.utils.BooleanConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import static com.rand.ishowApi.account.domain.repository.AccountSpecification.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountAdminService {

    private final AccountAdminDomainManager manager;
    private final AccountAdminMapper mapper;
    private final AccountRepository repository;
    private final KeyClockService keyClockService;

    public AccountAdminResponse createAccount(AccountAdminRequest request) {

        String userId = keyClockService.registerUser(mapper.fromAdmin(request));
        Account account = manager.createAdminAccount(request, userId);
        repository.save(account);
        return mapper.toResponse(account);
    }

    public AccountAdminResponse updateAccount(AccountAdminRequest request){

        Account account = repository.findById(request.id())
                .orElseThrow(() -> new CustomException("404_notFound", null));

        manager.updateAdminAccount(account, request);
        repository.save(account);
        return mapper.toResponse(account);
    }

    public AccountAdminResponse activateAccount(Long accountId, AccountStatus status){
        Account account = repository.findById(accountId)
                .orElseThrow(() -> new CustomException("404_notFound", null));

        manager.setActiveAdminAccount(account, status);
        repository.save(account);
        return mapper.toResponse(account);
    }

    public AccountAdminResponse changePassword(ChangePasswordRequest request){

        Account account = repository.findAccountByUserId(keyClockService.getCurrentUserId())
                .orElseThrow(() -> new CustomException("404_notFound", null));
        keyClockService.resetPassword(account.getUserId(), request.password());
        return mapper.toResponse(account);
    }

    public Page<AccountAdminResponse> filterAccount(AccountAdminFilterRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage() -1,
                request.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        Specification<Account> specification = Specification
                .where(hasActive(request.getStatus()))
                .and(hasRole(request.getRole()))
                .and(hasUserName(request.getUserName()));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);
    }
}
