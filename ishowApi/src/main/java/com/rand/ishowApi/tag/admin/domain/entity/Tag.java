package com.rand.ishowApi.tag.admin.domain.entity;


import com.rand.ishowApi.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(
        name = "tags",
        indexes = {
                @Index(name = "idx_tags_title_en", columnList = "title_en"),
                @Index(name = "idx_tags_title_ar", columnList = "title_ar")
        }
)
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title_ar", nullable = false)
    private String titleAr;
    @Column(name = "title_en", nullable = false)
    private String titleEn;
    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean common = false;
}
