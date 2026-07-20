package com.rand.ishowApi.account.domain.entity;

import com.rand.ishowApi.audit.BaseEntity;
import com.rand.ishowApi.shared.gender.Gender;
import com.rand.ishowApi.shared.language.Language;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(
        name = "accounts_profile",
        indexes = {
                @Index(name = "idx_profile_account_id", columnList = "account_id"),
                @Index(name = "idx_profile_language", columnList = "language")
        }
)
public class AccountProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "photo_path")
    private String photoPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "mobile_model")
    private String MobileModel;

    @Column(name = "mobile_manufacturer")
    private String mobileManufacturer;

    @Column(name="platform")
    private String platform;



}

