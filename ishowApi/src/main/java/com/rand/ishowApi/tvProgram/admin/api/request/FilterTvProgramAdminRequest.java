package com.rand.ishowApi.tvProgram.admin.api.request;

import com.rand.ishowApi.common.PaginationFilter;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import lombok.Data;

@Data
public class FilterTvProgramAdminRequest extends PaginationFilter {
    private String name;
    private Long accountId;
    private String isActive;
    private String isPublish;
    private String isKids;
    private String isSport;
    private String hasRight;
    private Badge badge;
    private ContentLanguage language;
}

