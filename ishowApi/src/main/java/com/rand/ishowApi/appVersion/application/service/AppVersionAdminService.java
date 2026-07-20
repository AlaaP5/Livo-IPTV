package com.rand.ishowApi.appVersion.application.service;

import com.rand.ishowApi.appVersion.api.admin.request.AppVersionAdminRequest;
import com.rand.ishowApi.appVersion.application.manager.AppVersionDomainManager;
import com.rand.ishowApi.appVersion.domain.entity.AppVersion;
import com.rand.ishowApi.appVersion.domain.repository.AppVersionRepository;
import com.rand.ishowApi.exception.model.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.rand.ishowApi.response.ApiResponseCode.VERSION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AppVersionAdminService {
    private final AppVersionRepository repository;
    private final AppVersionDomainManager manager;

    public AppVersion create(AppVersionAdminRequest request) {
        return repository.save(manager.create(request));
    }
    public AppVersion update( AppVersionAdminRequest request) {
        AppVersion version = repository.findById(request.getId())
                .orElseThrow(() -> new CustomException(VERSION_NOT_FOUND,null));
        manager.update(version, request);
        repository.save(version);
        return version;
    }
    public AppVersion setActive(Long id, String active) {
        AppVersion version = repository.findById(id)
                .orElseThrow(() -> new CustomException(VERSION_NOT_FOUND,null));
        manager.setActive(version, active);
        repository.save(version);
        return version;
    }

}