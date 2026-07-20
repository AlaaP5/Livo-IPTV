package com.rand.ishowApi.series.mobile.api.response;


import lombok.Data;

@Data
public class SeasonListResponse {
    private String seasonId;
    private String title;
    private String description;
    private String poster;
    private Integer releaseYear;
    private Integer number;
    private Double rating;
    private Integer episodeCount;


}
