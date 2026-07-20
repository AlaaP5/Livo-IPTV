package com.rand.ishowApi.tag.mobile.mapper;

import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import com.rand.ishowApi.tag.mobile.api.response.TagMobileResponse;
import com.rand.ishowApi.utils.LocalizedText;
import org.springframework.stereotype.Component;

@Component
public class TagMobileMapper {

    public TagMobileResponse toResponse(Tag tag) {
        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();

        TagMobileResponse response = new TagMobileResponse();
        response.setId(tag.getId());
        response.setTitle(LocalizedText.getName(tag.getTitleEn(), tag.getTitleAr(), lang));
        return response;
    }
}

