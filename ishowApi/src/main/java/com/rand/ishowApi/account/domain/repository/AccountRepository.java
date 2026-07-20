package com.rand.ishowApi.account.domain.repository;

import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.account.domain.entity.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
	Optional<Account> findByUserId(String userId);

	Optional<Account> findByUsername(String username);

	public Optional<Account> findAccountByUserId(String userId);

	Optional<Account> findByGsmAndStatusNot(String gsm, AccountStatus status);

	boolean existsByGsm(String gsm);

	boolean existsByGsmAndStatusNot(String gsm, AccountStatus status);
}

