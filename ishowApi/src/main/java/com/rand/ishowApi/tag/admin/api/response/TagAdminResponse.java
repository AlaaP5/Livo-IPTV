package com.rand.ishowApi.tag.admin.api.response;

import lombok.Data;

@Data
public class TagAdminResponse {
    private Long id;
    private String titleAr;
    private String titleEn;
    private boolean active;
    private boolean common;
}

