package com.rand.ishowApi.movie.mobile.api.response;

import com.rand.ishowApi.lookup.api.response.LookupPosterResponse;
import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import lombok.Data;

import java.util.List;

@Data
public class MovieDetailsResponse {
    private String movieId;
    private String title;
    private String description;

    private String poster;
    private String trailerUrl;
    private Badge badge;
    private List<LookupResponse> tags;

    private AccessType accessType;

    private String streamUrl;

    private String language;

    private List<String> subtitle;
    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;

    private String duration;

    private Integer releaseYear;

    private Double rating;

    private List<LookupPosterResponse> actors;

    private Boolean isWatchList;

    private Boolean isLike;

    private List<RelatedMovie> relatedMovies;
}
