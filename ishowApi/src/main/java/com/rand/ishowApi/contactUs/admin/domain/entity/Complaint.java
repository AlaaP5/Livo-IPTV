package com.rand.ishowApi.contactUs.admin.domain.entity;


import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "complaints",
        indexes = {
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_is_read", columnList = "is_read")
        }
)
public class Complaint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alternative_phone")
    private String alternativePhone;

    @Column(name = "email")
    private String email;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "is_read")
    private Boolean isRead = false;
}
