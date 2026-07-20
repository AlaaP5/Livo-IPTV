package com.rand.ishowApi.appVersion.api.mobile.response;

import com.rand.ishowApi.appVersion.domain.entity.VersionStatus;
import lombok.Data;

@Data
public class AppVersionResponse {
    private boolean active;
    private String version;
    private String platform;
    private String link;
    private VersionStatus versionStatus ;
}
