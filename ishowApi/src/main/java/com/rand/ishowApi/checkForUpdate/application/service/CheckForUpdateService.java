package com.rand.ishowApi.checkForUpdate.application.service;


import com.rand.ishowApi.appVersion.application.service.AppVersionMobileService;
import com.rand.ishowApi.checkForUpdate.api.response.CheckForUpdateResponse;
import com.rand.ishowApi.checkForUpdate.mapper.CheckForUpdateMapper;
import com.rand.ishowApi.security.context.DeviceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CheckForUpdateService {

    private final CheckForUpdateMapper mapper;
    private final AppVersionMobileService appVersionMobileService;


    public CheckForUpdateResponse checkForUpdate() {

        return mapper.toCheckForUpdateResponse(
                appVersionMobileService.check(DeviceContext.get().getPlatform(),DeviceContext.get().getAppVersion())

        );

    }
}
