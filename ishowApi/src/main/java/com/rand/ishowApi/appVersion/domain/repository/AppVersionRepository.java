package com.rand.ishowApi.appVersion.domain.repository;

import com.rand.ishowApi.appVersion.domain.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface  AppVersionRepository extends JpaRepository<AppVersion, Long> {
    Optional<AppVersion> findByPlatformAndVersion(String platform, String version);
    Optional<AppVersion> findTopByPlatformAndActiveTrueOrderByCreatedAtDesc(String platform);

    @Query("""
   select a
   from AppVersion a
   where trim(a.platform) = :platform
     and trim(a.version)  = :version
""")
    Optional<AppVersion> findAppVersion(
            @Param("platform") String platform,
            @Param("version") String version
    );
}