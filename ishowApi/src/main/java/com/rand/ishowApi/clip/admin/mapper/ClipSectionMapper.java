package com.rand.ishowApi.clip.admin.mapper;

import com.rand.ishowApi.clip.admin.api.response.ClipSectionAdminResponse;
import com.rand.ishowApi.clip.admin.domain.openSearch.model.ClipDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClipSectionMapper {

    public ClipSectionAdminResponse toClipSectionResponse(ClipDocument document) {
        if (document == null) {
            return null;
        }

        ClipSectionAdminResponse response = new ClipSectionAdminResponse();
        response.setClipId(document.getClipId());
        response.setTitleEn(document.getTitleEn());
        response.setTitleAr(document.getTitleAr());
        response.setPoster(document.getPoster());
        response.setCreateDate(document.getCreateDate());
        response.setIsTop(document.getIsTop());
        response.setIsKids(document.getIsKids());
        response.setIsSports(document.getIsSports());
        response.setIsPublish(document.getIsPublish());
        response.setDuration(document.getDuration());
        response.setBadge(document.getBadge());
        response.setAccessType(document.getAccessType());
        response.setTags(document.getTags());
        response.setSectionId(document.getSectionId());
        response.setSectionTitleAr(document.getSectionTitleAr());
        response.setSectionTitleEn(document.getSectionTitleEn());
        response.setSectionOrder(document.getSectionOrder());
        response.setSectionActive(document.isSectionActive());
        response.setSectionPublish(document.isSectionPublish());
        return response;
    }

    public List<ClipSectionAdminResponse> toClipSectionResponseList(List<ClipDocument> documents) {
        return documents.stream().map(this::toClipSectionResponse).collect(Collectors.toList());
    }
}

