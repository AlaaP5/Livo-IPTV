package com.rand.ishowApi.helpCenter.mobile.mapper;

import com.rand.ishowApi.helpCenter.admin.domain.entity.HelpCenter;
import com.rand.ishowApi.helpCenter.mobile.api.response.HelpCenterMobileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class HelpCenterMobileMapper {

    public HelpCenterMobileResponse toResponse(HelpCenter helpCenter) {

        HelpCenterMobileResponse response = new HelpCenterMobileResponse();

        response.setHelpCenterMetas(helpCenter.getHelpCenterMetas() != null
                ? helpCenter.getHelpCenterMetas()
                : Collections.emptyList());

        return response;
    }
}
