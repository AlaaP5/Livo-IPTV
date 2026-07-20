package com.rand.ishowApi.security.context;


import com.rand.ishowApi.shared.language.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfo {
    private String appVersion;
    private String mobileModel;
    private String mobileManufacturer;
    private String platform;
    private String osVersion;
    private String deviceId;
    private Language language ;
    private String hasRight;
}
