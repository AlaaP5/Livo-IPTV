package com.rand.ishowApi.checkForUpdate.api.response;


import com.rand.ishowApi.appVersion.api.mobile.response.AppVersionResponse;
import lombok.Data;


@Data
public class CheckForUpdateResponse {
    private AppVersionResponse appVersion;


}
