package com.rand.ishowApi.search.api.request;


import com.rand.ishowApi.metadata.ContentLanguage;
import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    private SearchIndex searchIndex;
    private String text;
    private List<Long> tags;
    private Boolean isKids;
    private Boolean isSports;
    private ContentLanguage language;
    private Double rating;
    private Integer releaseYear;
    private SortRequest sort;
    private Integer size;
    private Integer page;
}
