package com.rand.ishowApi.openSearch.response;


import lombok.Data;

import java.util.List;

@Data
public class SectionBannerResponse<T>  {
    private List<T> banner;
    private List<SectionResponse<T>> sections;
}
