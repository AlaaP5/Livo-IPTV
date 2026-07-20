package com.rand.ishowApi.helpCenter.admin.application.manager;


import com.rand.ishowApi.helpCenter.admin.domain.entity.HelpCenter;
import com.rand.ishowApi.metadata.HelpCenterMeta;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HelpCenterDomainManager {

    public HelpCenter update(List<HelpCenterMeta> helpCenterMetas) {
        return HelpCenter.builder()
                .helpCenterMetas(helpCenterMetas)
                .build();
    }
}
