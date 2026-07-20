package com.rand.ishowApi.channel.mobile.mapper;

import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.channel.admin.domain.entity.ChannelEpg;
import com.rand.ishowApi.channel.admin.domain.openSearch.model.ChannelDocument;
import com.rand.ishowApi.channel.mobile.api.response.ChannelDetailsResponse;
import com.rand.ishowApi.channel.mobile.api.response.ChannelEpgResponse;
import com.rand.ishowApi.channel.mobile.api.response.ChannelSectionResponse;
import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.openSearch.mapper.SectionContentMapper;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.utils.LocalizedText;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChannelMobileMapper {


    public ChannelDetailsResponse toChannelDetailsResponse(Channel channel, List<ChannelEpg> epgList, boolean isLke,  boolean isWatchList) {
        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();

        ChannelDetailsResponse response = new ChannelDetailsResponse();
        response.setChannelId(channel.getId());
        response.setTitle(LocalizedText.getName(channel.getTitleEn(), channel.getTitleAr(), lang));
        response.setDescription(LocalizedText.getName(channel.getDescriptionEn(), channel.getDescriptionAr(), lang));
        response.setStreamUrl(channel.getStreamUrl());
        response.setBadge(channel.getBadge());
        response.setIsLike(isLke);
        response.setIsWatchList(isWatchList);

        if (channel.getLogo() != null) {
            response.setLogo(channel.getLogo().getGeneratedPath());
        }

        if (channel.getTags() != null) {
            response.setTags(channel.getTags().stream().map(tag -> {
                LookupResponse tagResponse = new LookupResponse();
                tagResponse.setId(tag.getId());
                tagResponse.setName(LocalizedText.getName(tag.getTitleEn(), tag.getTitleAr(), lang));
                return tagResponse;
            }).toList());
        } else {
            response.setTags(Collections.emptyList());
        }

        response.setEpg(epgList == null ? Collections.emptyList() : epgList.stream().map(this::mapEpg).toList());

        return response;
    }

    public ChannelEpgResponse toChannelEpgResponse(ChannelEpg epg) {
        if (epg == null) {
            return null;
        }

        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();
        ChannelEpgResponse response = new ChannelEpgResponse();
        response.setId(epg.getId());
        response.setTitle(LocalizedText.getName(epg.getTitleEn(), epg.getTitleAr(), lang));
        response.setStartTime(epg.getStartDate());
        response.setEndTime(epg.getEndDate());
        return response;
    }

    private ChannelEpgResponse mapEpg(ChannelEpg epg) {
        return toChannelEpgResponse(epg);
    }
}

