package com.rand.ishowApi.appVersion.domain.entity;

import com.rand.ishowApi.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "app_versions", indexes = {
        @Index(name = "idx_app_versions_active", columnList = "active"),
        @Index(name = "idx_app_version_platform", columnList = "platform"),
        @Index(name = "idx_app_versions_active", columnList = "version"),

})
@Getter
@Setter
public class AppVersion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private String platform; // Android, iOS, Web

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "link")
    private String link;     // download/update link
}