package com.rand.ishowApi.channel.admin.application.manager;

import com.rand.ishowApi.channel.admin.api.request.ChannelEpgAdminRequest;
import com.rand.ishowApi.channel.admin.domain.entity.ChannelEpg;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

@Component
public class ChannelEpgDomainManager {

    public ChannelEpg create(ChannelEpgAdminRequest req) {
        return ChannelEpg.builder()
                .channelId(req.getChannelId())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .titleEn(req.getTitleEn())
                .titleAr(req.getTitleAr())
                .active(BooleanConverter.getActiveBoolean(req.getActive()))
                .build();
    }

    public void update(ChannelEpg e, ChannelEpgAdminRequest req) {

        if (req.getChannelId() != null) {
            e.setChannelId(req.getChannelId());
        }

        if (req.getStartDate() != null) {
            e.setStartDate(req.getStartDate());
        }

        if (req.getEndDate() != null) {
            e.setEndDate(req.getEndDate());
        }

        if (req.getTitleEn() != null) {
            e.setTitleEn(req.getTitleEn());
        }

        if (req.getTitleAr() != null) {
            e.setTitleAr(req.getTitleAr());
        }

        if (req.getActive() != null) {
            e.setActive(BooleanConverter.getActiveBoolean(req.getActive()));
        }
    }

    public void setActive(ChannelEpg e, String active) {
        e.setActive(BooleanConverter.getActiveBoolean(active));
    }


    public ChannelEpg createFromParsedXml(ChannelEpgAdminRequest request) {
        return ChannelEpg.builder()
                .channelId(request.getChannelId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .titleEn(request.getTitleEn())
                .titleAr(request.getTitleAr())
                .active(Boolean.TRUE)
                .build();
    }

}

