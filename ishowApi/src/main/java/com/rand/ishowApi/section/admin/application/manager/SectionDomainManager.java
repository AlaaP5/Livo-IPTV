package com.rand.ishowApi.section.admin.application.manager;

import com.rand.ishowApi.section.admin.api.request.SectionAdminRequest;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

@Component
public class SectionDomainManager {

    public Section create(SectionAdminRequest request) {
        Section section = new Section();
        section.setTitleEn(request.getTitleEn());
        section.setTitleAr(request.getTitleAr());
        section.setOrder(request.getOrder());
        section.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        section.setPublish(false);
        section.setPage(request.getPage());
        section.setContentType(request.getContentType());
        return section;
    }

    public void update(Section section, SectionAdminRequest request) {
        section.setTitleEn(request.getTitleEn());
        section.setTitleAr(request.getTitleAr());
        section.setOrder(request.getOrder());
        section.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
//        section.setPublish(false);
        section.setPage(request.getPage());
        section.setContentType(request.getContentType());
    }

    public void setActive(Section section, String active) {
        section.setActive(BooleanConverter.getActiveBoolean(active));
    }

    public void setPublish(Section section, String publish) {
        section.setPublish(BooleanConverter.getActiveBoolean(publish));
    }
}

