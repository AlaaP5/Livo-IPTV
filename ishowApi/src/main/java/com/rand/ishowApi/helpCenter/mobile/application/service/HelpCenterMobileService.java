package com.rand.ishowApi.helpCenter.mobile.application.service;

import com.rand.ishowApi.helpCenter.admin.domain.entity.HelpCenter;
import com.rand.ishowApi.helpCenter.admin.domain.repository.HelpCenterRepository;
import com.rand.ishowApi.helpCenter.mobile.api.response.HelpCenterMobileResponse;
import com.rand.ishowApi.helpCenter.mobile.mapper.HelpCenterMobileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class HelpCenterMobileService {

    private final HelpCenterRepository helpCenterRepository;
    private final HelpCenterMobileMapper helpCenterMobileMapper;

    public HelpCenterMobileResponse getHelpCenter() {

        HelpCenter helpCenter = helpCenterRepository.findAll()
                .stream()
                .findFirst()
                .orElse(new HelpCenter());

        return helpCenterMobileMapper.toResponse(helpCenter);
    }
}
