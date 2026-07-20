package com.rand.ishowApi.home.mobile.api.response;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;
@JsonPropertyOrder({
        "banner",
        "sections"
})
@Data
public class HomeMobileResponse {
    private List<BannerResponse> banner;
    private List<HomeSectionResponse> sections;
}
