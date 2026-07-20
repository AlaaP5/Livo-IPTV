package com.rand.ishowApi.helpCenter.admin.application.service;

import com.rand.ishowApi.helpCenter.admin.api.request.HelpCenterAdminRequest;
import com.rand.ishowApi.helpCenter.admin.api.response.HelpCenterAdminResponse;
import com.rand.ishowApi.helpCenter.admin.domain.entity.HelpCenter;
import com.rand.ishowApi.helpCenter.admin.domain.repository.HelpCenterRepository;
import com.rand.ishowApi.helpCenter.admin.mapper.HelpCenterAdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelpCenterAdminService {

    private final HelpCenterRepository helpCenterRepository;
    private final HelpCenterAdminMapper helpCenterAdminMapper;

    public HelpCenterAdminResponse getHelpCenter() {
        HelpCenter helpCenter = helpCenterRepository.findAll()
                .stream()
                .findFirst()
                .orElse(new HelpCenter());

        return helpCenterAdminMapper.toResponse(helpCenter);
    }

    public HelpCenterAdminResponse updateHelpCenter(HelpCenterAdminRequest helpCenterAdminRequest) {

        HelpCenter helpCenter = helpCenterRepository.findAll()
                .stream()
                .findFirst()
                .orElse(new HelpCenter());

        helpCenter.setHelpCenterMetas(helpCenterAdminRequest.getHelpCenterMetas());

        HelpCenter savedHelpCenter = helpCenterRepository.save(helpCenter);

        return helpCenterAdminMapper.toResponse(savedHelpCenter);
    }
}
