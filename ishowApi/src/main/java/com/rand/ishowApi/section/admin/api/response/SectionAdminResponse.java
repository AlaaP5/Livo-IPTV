package com.rand.ishowApi.section.admin.api.response;

import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.shared.settings.MobilePage;
import lombok.Data;

@Data
public class SectionAdminResponse {
    private Long id;
    private String titleAr;
    private String titleEn;
    private Integer order;
    private boolean active;
    private boolean publish;
    private MobilePage page;
    private ContentType contentType;
}

