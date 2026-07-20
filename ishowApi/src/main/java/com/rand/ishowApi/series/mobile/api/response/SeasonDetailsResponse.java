package com.rand.ishowApi.series.mobile.api.response;


import com.rand.ishowApi.lookup.api.response.LookupPosterResponse;
import lombok.Data;

import java.util.List;

@Data
public class SeasonDetailsResponse extends SeasonListResponse {
    private String trailerUrl;
    private List<LookupPosterResponse> actors;
    private Boolean isWatchList;
    private List<EpisodeDetailsResponse> episodes;
}
