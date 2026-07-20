package com.rand.ishowApi.tag.admin.application.manager;

import com.rand.ishowApi.tag.admin.api.request.TagAdminRequest;
import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

@Component
public class TagDomainManager {

    public Tag create(TagAdminRequest request) {

        Tag tag = new Tag();
        tag.setTitleEn(request.getTitleEn());
        tag.setTitleAr(request.getTitleAr());
        tag.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        tag.setCommon(BooleanConverter.getActiveBoolean(request.getCommon()));

        return tag;
    }


    public void update(Tag tag, TagAdminRequest request) {

        tag.setTitleEn(request.getTitleEn());
        tag.setTitleAr(request.getTitleAr());
        tag.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        tag.setCommon(BooleanConverter.getActiveBoolean(request.getCommon()));

    }


    public void setActive(Tag existing, String active) {
        existing.setActive(BooleanConverter.getActiveBoolean(active));
    }

    public void setCommon(Tag existing, String common) {
        existing.setCommon(BooleanConverter.getActiveBoolean(common));
    }
}
