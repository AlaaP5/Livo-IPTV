package com.rand.ishowApi.checkForUpdate.mapper;


import com.rand.ishowApi.appVersion.api.mobile.response.AppVersionResponse;
import com.rand.ishowApi.checkForUpdate.api.response.CheckForUpdateResponse;
import org.springframework.stereotype.Component;


@Component
public class CheckForUpdateMapper {

    public CheckForUpdateResponse toCheckForUpdateResponse(AppVersionResponse appVersion) {

        CheckForUpdateResponse response = new CheckForUpdateResponse();
        response.setAppVersion(appVersion);

        return response;
    }


}
