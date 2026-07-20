package com.rand.ishowApi.section.admin.mapper;

import com.rand.ishowApi.section.admin.api.response.SectionAdminResponse;
import com.rand.ishowApi.section.domain.entity.Section;
import org.springframework.stereotype.Component;

@Component
public class SectionAdminMapper {

    public SectionAdminResponse toResponse(Section section) {
        SectionAdminResponse response = new SectionAdminResponse();
        response.setId(section.getId());
        response.setTitleEn(section.getTitleEn());
        response.setTitleAr(section.getTitleAr());
        response.setOrder(section.getOrder());
        response.setActive(section.getActive());
        response.setPublish(section.getPublish());
        response.setPage(section.getPage());
        response.setContentType(section.getContentType());
        return response;
    }
}

