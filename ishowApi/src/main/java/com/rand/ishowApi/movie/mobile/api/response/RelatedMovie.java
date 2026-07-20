package com.rand.ishowApi.movie.mobile.api.response;



import com.rand.ishowApi.metadata.Badge;
import lombok.Data;


@Data
public class RelatedMovie {
    private  String movieId;
    private  String title;
    private  String Poster;
    private  Badge badge;

}
