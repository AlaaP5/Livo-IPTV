package com.rand.ishowApi.home.mobile.api.response;


import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.shared.settings.ContentType;
import lombok.Data;

import java.util.List;

@Data
public class BannerResponse {
    private String id;
    private ContentType type;
    private String image;
    private String title;
    private List<LookupResponse> tags;
}

