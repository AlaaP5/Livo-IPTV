package com.rand.ishowApi.appVersion.application.service;

import com.rand.ishowApi.appVersion.api.mobile.response.AppVersionResponse;
import com.rand.ishowApi.appVersion.domain.entity.AppVersion;
import com.rand.ishowApi.appVersion.domain.repository.AppVersionRepository;
import com.rand.ishowApi.appVersion.mapper.AppVersionMapper;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.redis.service.RedisService;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AppVersionMobileService {

    private final AppVersionRepository repository;
    private final AppVersionMapper mapper;
    private final RedisService redisService;

    public AppVersionResponse check(String platform, String version) {

        // version provided by client
        AppVersion clientVersion = getActiveVersion(platform, version);

        // latest active version for the platform
        AppVersion latest = getLatestVersion(platform);

        checkIsActiveVersion(clientVersion);


        return mapper.toResponse(clientVersion, version, latest);
    }

    public AppVersion getActiveVersion(String platform, String version) {

        AppVersion appVersion = redisService.getKey("getActiveVersion", AppVersion.class, platform, version);
        if (appVersion != null) {
            return appVersion;
        }

        appVersion = repository
                .findAppVersion(platform, version)
                .orElseThrow(() ->
                        new CustomException(ApiResponseCode.VERSION_NOT_VALID, null)
                );

        redisService.setKey("getActiveVersion", appVersion, Duration.ofHours(6), platform, version);

        return appVersion;
    }

    private void checkIsActiveVersion(AppVersion version) {
        if (!Boolean.TRUE.equals(version.getActive())) {
            throw new CustomException(ApiResponseCode.VERSION_NOT_VALID, null);
        }
    }

    public AppVersion getLatestVersion(String platform) {

        AppVersion appVersion = redisService.getKey("getLatestVersion", AppVersion.class, platform);
        if (appVersion != null) {
            return appVersion;
        }

        appVersion = repository
                .findTopByPlatformAndActiveTrueOrderByCreatedAtDesc(platform)
                .orElseThrow(() ->
                        new CustomException(ApiResponseCode.VERSION_NOT_FOUND, null)
                );

        // ✅ Use correct key
        redisService.setKey("getLatestVersion", appVersion, Duration.ofHours(6), platform);

        return appVersion;
    }
}