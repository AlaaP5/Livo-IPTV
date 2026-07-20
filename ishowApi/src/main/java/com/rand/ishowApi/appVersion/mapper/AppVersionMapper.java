package com.rand.ishowApi.appVersion.mapper;


import com.rand.ishowApi.appVersion.api.mobile.response.AppVersionResponse;
import com.rand.ishowApi.appVersion.domain.entity.AppVersion;
import com.rand.ishowApi.appVersion.domain.entity.VersionStatus;
import com.rand.ishowApi.utils.VersionComparator;
import org.springframework.stereotype.Component;

@Component
public class AppVersionMapper {

    public AppVersionResponse toResponse(AppVersion clientVersion, String clientVersionStr, AppVersion latestVersion) {
        AppVersionResponse response = new AppVersionResponse();
        response.setVersion(clientVersion.getVersion());
        response.setPlatform(clientVersion.getPlatform());
        response.setActive(clientVersion.getActive());
        response.setLink(clientVersion.getLink());
        response.setVersionStatus(getVersionStatus(clientVersion, latestVersion));
        return response;
    }

    private VersionStatus getVersionStatus(AppVersion clientVersion, AppVersion latestVersion) {
        if (!Boolean.TRUE.equals(clientVersion.getActive())) {
            return VersionStatus.FORCE_UPDATE;
        }

        int result = VersionComparator.compare(clientVersion.getVersion(), latestVersion.getVersion());
        if (result < 0) {
            return VersionStatus.OPTIONAL_UPDATE;
        }

        return VersionStatus.NO_NEED_UPDATE;
    }
}
