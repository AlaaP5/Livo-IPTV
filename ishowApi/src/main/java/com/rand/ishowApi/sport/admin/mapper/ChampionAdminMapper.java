package com.rand.ishowApi.sport.admin.mapper;

import com.rand.ishowApi.sport.admin.api.response.ChampionAdminResponse;
import com.rand.ishowApi.sport.admin.domain.entity.Champion;
import org.springframework.stereotype.Component;


@Component
public class ChampionAdminMapper {

    public ChampionAdminResponse toResponse(Champion champion) {
        ChampionAdminResponse response = new ChampionAdminResponse();
        response.setId(champion.getId());
        response.setNameEn(champion.getNameEn());
        response.setNameAr(champion.getNameAr());
        response.setLogoPath(champion.getGeneratedLogoPath());
        response.setActive(champion.isActive());
        return response;
    }
}
