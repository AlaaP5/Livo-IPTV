package com.rand.ishowApi.account.domain.repository;

import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.account.domain.entity.AccountRole;
import com.rand.ishowApi.account.domain.entity.AccountStatus;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecification {

    public static Specification<Account> hasActive(AccountStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Account> hasRole(AccountRole role) {
        return (root, query, cb) -> {
            if (role == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("role"), role);
        };
    }

    public static Specification<Account> hasUserName(String userName) {
        return (root, query, cb) -> {
            if (userName == null || userName.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("username")),
                    "%" + userName.toLowerCase() + "%"
            );
        };
    }
}
