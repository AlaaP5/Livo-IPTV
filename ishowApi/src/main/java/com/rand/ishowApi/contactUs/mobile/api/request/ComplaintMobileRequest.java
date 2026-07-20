package com.rand.ishowApi.contactUs.mobile.api.request;

import lombok.Data;

@Data
public class ComplaintMobileRequest {

    private String alternativePhone;
    private String email;
    private String title;
    private String description;
}
