package com.rand.ishowApi.tvProgram.mobile.api.response;


import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import lombok.Data;

import java.util.List;

@Data
public class TvProgramDetailsResponse {
    private String tvProgramId;
    private String title;
    private String description;
    private String poster;
    private Badge badge;
    private String trailerUrl;
    private List<LookupResponse> tags;
    private String language;
    private Integer releaseYear;
    private Integer seasonCount;
    private java.util.List<ContentLanguage> subtitleLanguages;
    private java.util.List<ContentLanguage> audioLanguages;
    private List<TVProgramSeasonListResponse> seasons;
    private List<TvProgramDetailsResponse> related;

    private Boolean isWatchList;
    private Boolean isLike;
}
