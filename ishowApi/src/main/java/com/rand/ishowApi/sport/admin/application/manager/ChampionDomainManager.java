package com.rand.ishowApi.sport.admin.application.manager;

import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.sport.admin.api.request.ChampionAdminRequest;
import com.rand.ishowApi.sport.admin.domain.entity.Champion;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;
@Component
public class ChampionDomainManager {
    public Champion createChampion(ChampionAdminRequest request) {
        Champion champion = new Champion();
        champion.setActive(BooleanConverter.getActiveBoolean(request.active()));
        champion.setNameAr(request.nameAr());
        champion.setNameEn(request.nameEn());
        champion.setFullLogoPath("");
        champion.setGeneratedLogoPath("");
        return champion;
    }
    public void updateChampion(ChampionAdminRequest request, Champion champion, OriginalUploadMetadata logo) {
        champion.setActive(BooleanConverter.getActiveBoolean(request.active()));
        champion.setNameEn(request.nameEn());
        champion.setNameAr(request.nameAr());
        if (logo != null) {
            champion.setFullLogoPath(logo.getFullPath());
            champion.setGeneratedLogoPath(logo.getGeneratedPath());
        }
    }
    public void addChampionLogo(Champion champion, OriginalUploadMetadata logo) {
        champion.setGeneratedLogoPath(logo.getGeneratedPath());
        champion.setFullLogoPath(logo.getFullPath());
    }
    public void activateChampion(Champion champion, String active) {
        champion.setActive(BooleanConverter.getActiveBoolean(active));
    }
}