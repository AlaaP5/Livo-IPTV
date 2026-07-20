package com.rand.ishowApi.tag.admin.api.request;

import lombok.Data;

@Data
public class TagAdminRequest {
    private Long id;
    private String titleAr;
    private String titleEn;
    private String active;
    private String common;
}
