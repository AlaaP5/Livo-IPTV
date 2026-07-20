package com.rand.ishowApi.tvProgram.mobile.api.response;


import lombok.Data;

import java.util.List;

@Data
public class TvProgramSeasonDetailsResponse extends TVProgramSeasonListResponse {
    private String trailerUrl;
    private Boolean isWatchList;
    private List<TvProgramEpisodeDetailsResponse> episodes;
}
