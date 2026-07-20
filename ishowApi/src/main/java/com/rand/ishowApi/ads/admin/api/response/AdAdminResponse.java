package com.rand.ishowApi.ads.admin.api.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdAdminResponse {

    private Long id;
    private String imagePath;
    private String deepLink;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
}
