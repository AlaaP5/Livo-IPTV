package com.rand.ishowApi.sport.admin.domain.entity;

import com.rand.ishowApi.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(
        name = "champions",
        indexes = {
                @Index(name = "idx_champion_active", columnList = "active")
        }
)
public class Champion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_ar", nullable = false)
    private String nameAr;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "full_logo_path")
    private String fullLogoPath;

    @Column(name = "generated_logo_path")
    private String generatedLogoPath;

    @Column(nullable = false)
    private boolean active;
}


