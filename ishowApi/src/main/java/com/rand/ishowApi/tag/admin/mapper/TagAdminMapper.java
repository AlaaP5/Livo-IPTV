package com.rand.ishowApi.tag.admin.mapper;

import com.rand.ishowApi.tag.admin.api.response.TagAdminResponse;
import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import org.springframework.stereotype.Component;


@Component
public class TagAdminMapper {
    public TagAdminResponse toResponse(Tag tag) {
        TagAdminResponse response = new TagAdminResponse();
        response.setId(tag.getId());
        response.setTitleEn(tag.getTitleEn());
        response.setTitleAr(tag.getTitleAr());
        response.setActive(tag.isActive());
        response.setCommon(tag.isCommon());
        return response;
    }
}
