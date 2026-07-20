package com.rand.ishowApi.actor.domain.entity;

import com.rand.ishowApi.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(
        name = "actors",
        indexes = {
                @Index(name = "idx_actor_active", columnList = "active")
        }
)
public class Actor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_ar", nullable = false)
    private String nameAr;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "full_image_path", nullable = true)
    private String fullImagePath;

    @Column(name = "generated_image_path", nullable = true)
    private String generatedImagePath;

    @Column(nullable = false)
    private boolean active;
}
