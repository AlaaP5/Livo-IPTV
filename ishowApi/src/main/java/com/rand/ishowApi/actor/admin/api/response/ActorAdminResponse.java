package com.rand.ishowApi.actor.admin.api.response;

import lombok.Data;

@Data
public class ActorAdminResponse {

    private Long id;
    private String nameEn;
    private String nameAr;
    private String imagePath;
    private Boolean active;
}
