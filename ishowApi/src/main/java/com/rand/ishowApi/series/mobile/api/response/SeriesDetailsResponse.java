package com.rand.ishowApi.series.mobile.api.response;


import com.rand.ishowApi.lookup.api.response.LookupPosterResponse;
import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import lombok.Data;

import java.util.List;

@Data
public class SeriesDetailsResponse {
    private String seriesId;
    private String title;
    private String description;
    private String poster;
    private Badge badge;
    private String trailerUrl;
    private List<LookupResponse> tags;
    private List<LookupPosterResponse> actors;
    private AccessType accessType;
    private String language;
    private Integer releaseYear;
    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;
    private Double rating;
    private List<SeriesDetailsResponse> related;
    private List<SeasonListResponse> seasons;

    private Boolean isWatchList;
    private Boolean isLike;

}
