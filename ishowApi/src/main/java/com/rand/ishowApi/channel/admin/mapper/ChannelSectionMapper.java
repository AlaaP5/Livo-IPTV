package com.rand.ishowApi.channel.admin.mapper;

import com.rand.ishowApi.channel.admin.api.response.ChannelAdminSectionResponse;
import com.rand.ishowApi.channel.admin.domain.openSearch.model.ChannelDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChannelSectionMapper {

    public ChannelAdminSectionResponse toChannelSectionResponse(ChannelDocument document) {
        if (document == null) {
            return null;
        }

        ChannelAdminSectionResponse response = new ChannelAdminSectionResponse();
        response.setChannelId(document.getChannelId());
        response.setTitleEn(document.getTitleEn());
        response.setTitleAr(document.getTitleAr());
        response.setLogo(document.getLogo());
        response.setCreateDate(document.getCreateDate());
        response.setIsTop(document.getIsTop());
        response.setIsKids(document.getIsKids());
        response.setIsSports(document.getIsSports());
        response.setIsPublish(document.getIsPublish());
        response.setBadge(document.getBadge());
        response.setTags(document.getTags());
        response.setSectionId(document.getSectionId());
        response.setSectionTitleAr(document.getSectionTitleAr());
        response.setSectionTitleEn(document.getSectionTitleEn());
        response.setSectionOrder(document.getSectionOrder());
        response.setSectionActive(document.isSectionActive());
        response.setSectionPublish(document.isSectionPublish());
        return response;
    }

    public List<ChannelAdminSectionResponse> toChannelSectionResponseList(List<ChannelDocument> documents) {
        return documents.stream().map(this::toChannelSectionResponse).collect(Collectors.toList());
    }
}

