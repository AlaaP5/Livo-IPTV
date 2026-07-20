package com.rand.ishowApi.tag.admin.api.request;

import com.rand.ishowApi.common.PaginationFilter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TagFilterAdminRequest extends PaginationFilter {
    private String active;
    private String title;
    private String common;
}
