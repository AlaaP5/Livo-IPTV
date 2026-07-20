package com.rand.ishowApi.helpCenter.admin.mapper;

import com.rand.ishowApi.helpCenter.admin.api.response.HelpCenterAdminResponse;
import com.rand.ishowApi.helpCenter.admin.domain.entity.HelpCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class HelpCenterAdminMapper {

    public HelpCenterAdminResponse toResponse(HelpCenter helpCenter) {

        HelpCenterAdminResponse response = new HelpCenterAdminResponse();

        response.setHelpCenterMetas(helpCenter.getHelpCenterMetas() != null
                ? helpCenter.getHelpCenterMetas()
                : Collections.emptyList());

        return response;
    }
}
