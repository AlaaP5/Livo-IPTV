package com.rand.ishowApi.account.domain.repository;

import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.account.domain.entity.AccountProfile;
import com.rand.ishowApi.account.domain.entity.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountProfileRepository extends JpaRepository<AccountProfile, Long> {
    Optional<AccountProfile> findByAccountId(Long accountId);

    Optional<AccountProfile> findByAccount(Account account);
}

