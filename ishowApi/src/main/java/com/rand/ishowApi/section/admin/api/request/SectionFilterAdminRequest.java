package com.rand.ishowApi.section.admin.api.request;

import com.rand.ishowApi.common.PaginationFilter;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.shared.settings.MobilePage;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SectionFilterAdminRequest extends PaginationFilter {
    private String active;
    private String publish;
    private String title;
    private MobilePage sectionPage;
    private ContentType contentType;
}

