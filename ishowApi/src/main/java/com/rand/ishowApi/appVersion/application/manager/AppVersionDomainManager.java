package com.rand.ishowApi.appVersion.application.manager;


import com.rand.ishowApi.appVersion.api.admin.request.AppVersionAdminRequest;
import com.rand.ishowApi.appVersion.domain.entity.AppVersion;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

@Component
public class AppVersionDomainManager {

    public AppVersion create(AppVersionAdminRequest request) {
        AppVersion v = new AppVersion();
        v.setPlatform(request.getPlatform());
        v.setVersion(request.getVersion());
        v.setActive(BooleanConverter.getActiveBoolean(request.getActive()) );
        v.setLink(request.getLink());
        return v;
    }

    public void update(AppVersion existing, AppVersionAdminRequest request) {
        if (request.getVersion() != null) existing.setVersion(request.getVersion());
        if (request.getPlatform() != null) existing.setPlatform(request.getPlatform());
        if (request.getActive() != null) existing.setActive(BooleanConverter.getActiveBoolean(request.getActive()) );
        if (request.getLink() != null) existing.setLink(request.getLink());
    }

    public void setActive(AppVersion version, String active) {
        version.setActive(BooleanConverter.getActiveBoolean(active));
    }
}