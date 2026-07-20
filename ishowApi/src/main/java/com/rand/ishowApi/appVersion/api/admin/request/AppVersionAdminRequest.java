package com.rand.ishowApi.appVersion.api.admin.request;


import lombok.Data;

@Data
public class AppVersionAdminRequest {
    private Long id;
    private String platform;
    private String version;
    private String active;
    private String link;
}
