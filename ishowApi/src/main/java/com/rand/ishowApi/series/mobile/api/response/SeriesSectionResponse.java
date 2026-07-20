package com.rand.ishowApi.series.mobile.api.response;


import com.rand.ishowApi.metadata.Badge;
import lombok.Data;

import java.util.List;

@Data
public class SeriesSectionResponse {
    private String  seriesId;
    private String title;
    private Badge badge;
    private double rating;
    private String poster;
    private List<String> tags;
    private Integer releaseYear;
    private Integer seasonCount;
}
