package com.rand.ishowApi.ads.admin.mapper;

import com.rand.ishowApi.ads.admin.api.response.AdAdminResponse;
import com.rand.ishowApi.ads.domain.entity.Ad;
import org.springframework.stereotype.Component;

@Component
public class AdAdminMapper {

    public AdAdminResponse toResponse(Ad ad) {

        AdAdminResponse response = new AdAdminResponse();

        response.setId(ad.getId());
        response.setImagePath(ad.getGeneratedImagePath());
        response.setDeepLink(ad.getDeepLink());
        response.setStartDate(ad.getStartDate());
        response.setEndDate(ad.getEndDate());
        response.setActive(ad.getActive());

        return response;
    }
}
