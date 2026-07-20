package com.rand.ishowApi.section.admin.api.request;

import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.shared.settings.MobilePage;
import lombok.Data;

@Data
public class SectionAdminRequest {
    private Long id;
    private String titleAr;
    private String titleEn;
    private Integer order;
    private String active;
    private MobilePage page;
    private ContentType contentType;
}

