package com.rand.ishowApi.kids.mobile.api.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@JsonPropertyOrder({
        "banner",
        "sections"
})
@Data
public class KidsMobileResponse {
    private List<BannerResponse> banner;
    private List<KidsSectionResponse> sections;
}

