package com.rand.ishowApi.ads.domain.entity;


import com.rand.ishowApi.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(
        name = "ads",
        indexes = {
                @Index(name = "idx_ad_active", columnList = "active")
        }
)
public class Ad extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_image_path", nullable = true)
    private String fullImagePath;

    @Column(name = "generated_image_path", nullable = true)
    private String generatedImagePath;

    @Column(name = "deep_link", nullable = false)
    private String deepLink;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean active;
}
