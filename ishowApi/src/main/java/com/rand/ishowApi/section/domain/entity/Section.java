package com.rand.ishowApi.section.domain.entity;

import com.rand.ishowApi.audit.BaseEntity;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.shared.settings.MobilePage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "sections",
        indexes = {
                @Index(name = "idx_sections_title_en", columnList = "title_en"),
                @Index(name = "idx_sections_title_ar", columnList = "title_ar"),
                @Index(name = "idx_sections_page", columnList = "page"),
                @Index(name = "idx_sections_content_type", columnList = "content_type"),
                @Index(name = "idx_sections_active", columnList = "active"),
                @Index(name = "idx_sections_publish", columnList = "publish")
        }
)
public class Section extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title_ar", nullable = false)
    private String titleAr;

    @Column(name = "title_en", nullable = false)
    private String titleEn;

    @Column(name = "sort_order")
    private Integer order;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Boolean publish ;

    @Enumerated(EnumType.STRING)
    @Column(name = "page")
    private MobilePage page;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;
}

