package com.rand.ishowApi.movie.mobile.api.response;


import com.rand.ishowApi.metadata.Badge;
import lombok.Data;

import java.util.List;

@Data
public class MovieSectionResponse {
    private String movieId;
    private String title;
    private Badge badge;
    private double rating;
    private String poster;
    private Integer releaseYear;
    private Integer duration;
    private List<String> tags;
}
