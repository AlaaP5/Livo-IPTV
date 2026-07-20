package com.rand.ishowApi.account.domain.entity;


import com.rand.ishowApi.audit.BaseEntity;
import com.rand.ishowApi.shared.operator.Operator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "accounts",
        indexes = {
                @Index(name = "idx_account_user_id", columnList = "user_id"),
                @Index(name = "idx_account_status", columnList = "status"),
                @Index(name = "idx_account_gsm", columnList = "gsm"),
                @Index(name = "idx_account_operator", columnList = "operator"),
                @Index(name = "idx_account_username", columnList = "username"),
        }
)
public class Account  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String gsm;
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    private String otp;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    private Operator operator;

}
