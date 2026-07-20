package com.rand.ishowApi.ads.admin.application.manager;


import com.rand.ishowApi.ads.admin.api.request.AdAdminRequest;
import com.rand.ishowApi.ads.domain.entity.Ad;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

@Component
public class AdDomainManger {

    public Ad createAd(AdAdminRequest request) {

        Ad ad = new Ad();

        ad.setActive(BooleanConverter.getActiveBoolean(request.active()));
        ad.setDeepLink(request.deepLink());
        ad.setStartDate(request.startDate());
        ad.setEndDate(request.endDate());
        ad.setFullImagePath("");
        ad.setGeneratedImagePath("");

        return ad;
    }

    public void updateAd(AdAdminRequest request, Ad ad, OriginalUploadMetadata poster) {

        ad.setActive(BooleanConverter.getActiveBoolean(request.active()));
        if(poster != null) {
            ad.setFullImagePath(poster.getFullPath());
            ad.setGeneratedImagePath(poster.getGeneratedPath());
        }
        ad.setDeepLink(request.deepLink());
        ad.setStartDate(request.startDate());
        ad.setEndDate(request.endDate());
    }

    public void addAdPoster(Ad ad, OriginalUploadMetadata poster) {
        ad.setGeneratedImagePath(poster.getGeneratedPath());
        ad.setFullImagePath(poster.getFullPath());
    }

    public void activateAd(Ad ad, String active) {

        ad.setActive(BooleanConverter.getActiveBoolean(active));
    }
}
