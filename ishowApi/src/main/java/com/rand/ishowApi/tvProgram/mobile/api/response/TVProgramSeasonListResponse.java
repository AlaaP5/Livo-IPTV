package com.rand.ishowApi.tvProgram.mobile.api.response;


import lombok.Data;

@Data
public class TVProgramSeasonListResponse {
    private String seasonId;
    private String title;
    private String description;
    private String poster;
    private Integer releaseYear;
    private Integer number;
    private Integer episodeCount;
}
