package com.rand.ishowApi.search.api.response;

import com.rand.ishowApi.channel.mobile.api.response.ChannelSectionResponse;
import com.rand.ishowApi.clip.mobile.api.response.ClipSectionResponse;
import com.rand.ishowApi.movie.mobile.api.response.MovieSectionResponse;
import com.rand.ishowApi.series.mobile.api.response.SeriesSectionResponse;
import com.rand.ishowApi.tvProgram.mobile.api.response.TvProgramSectionResponse;
import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {
    List<MovieSectionResponse> movies;
    List<SeriesSectionResponse> series;
    List<TvProgramSectionResponse> tvProgram;
    List<ClipSectionResponse> clips;
    List<ChannelSectionResponse> channels;

}
