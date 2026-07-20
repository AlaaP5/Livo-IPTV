package com.rand.ishowApi.search.mapper;


import com.fasterxml.jackson.databind.JsonNode;
import com.rand.ishowApi.channel.admin.domain.openSearch.model.ChannelDocument;
import com.rand.ishowApi.channel.mobile.mapper.ChannelDocMapper;
import com.rand.ishowApi.clip.admin.domain.openSearch.model.ClipDocument;
import com.rand.ishowApi.clip.mobile.mapper.ClipDocMapper;
import com.rand.ishowApi.movie.admin.domain.openSearch.model.MovieDocument;
import com.rand.ishowApi.movie.mobile.mapper.MovieDocMapper;
import com.rand.ishowApi.openSearch.mapper.SectionAggregationMapper;
import com.rand.ishowApi.search.api.response.SearchResponse;
import com.rand.ishowApi.series.admin.domain.openSearch.model.SeriesDocument;
import com.rand.ishowApi.series.mobile.mapper.SeriesDocMapper;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.model.TvProgramDocument;
import com.rand.ishowApi.tvProgram.mobile.mapper.TvProgramDocMapper;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.core.MsearchResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SearchMapper {

    private final SectionAggregationMapper sectionAggregationMapper;
    private final MovieDocMapper movieDocMapper;
    private final SeriesDocMapper seriesDocMapper;
    private final TvProgramDocMapper tvProgramDocMapper;
    private final ClipDocMapper clipDocMapper;
    private final ChannelDocMapper channelDocMapper;

    public SearchResponse toSearchResponse(MsearchResponse<JsonNode> response, String lang) {
        SearchResponse searchResponse = new SearchResponse();

        if (response == null || response.responses() == null) {
            searchResponse.setMovies(Collections.emptyList());
            searchResponse.setSeries(Collections.emptyList());
            searchResponse.setTvProgram(Collections.emptyList());
            searchResponse.setClips(Collections.emptyList());
            searchResponse.setChannels(Collections.emptyList());
            return searchResponse;
        }

        searchResponse.setMovies(sectionAggregationMapper.mapSearchItems(response, 0, MovieDocument.class, movieDocMapper, this::normalizeMovie, lang));
        searchResponse.setSeries(sectionAggregationMapper.mapSearchItems(response, 1, SeriesDocument.class, seriesDocMapper, this::normalizeSeries, lang));
        searchResponse.setTvProgram(sectionAggregationMapper.mapSearchItems(response, 2, TvProgramDocument.class, tvProgramDocMapper, this::normalizeTvProgram, lang));
        searchResponse.setClips(sectionAggregationMapper.mapSearchItems(response, 3, ClipDocument.class, clipDocMapper, this::normalizeClip, lang));
        searchResponse.setChannels(sectionAggregationMapper.mapSearchItems(response, 4, ChannelDocument.class, channelDocMapper, this::normalizeChannel, lang));

        return searchResponse;
    }

    private void normalizeMovie(MovieDocument document, JsonNode source) {
        if (document.getMovieId() == null) {
            document.setMovieId(readId(source));
        }
    }

    private void normalizeSeries(SeriesDocument document, JsonNode source) {
        if (document.getSeriesId() == null) {
            document.setSeriesId(readId(source));
        }
    }

    private void normalizeTvProgram(TvProgramDocument document, JsonNode source) {
        if (document.getTvProgramId() == null) {
            document.setTvProgramId(readId(source));
        }
    }

    private void normalizeClip(ClipDocument document, JsonNode source) {
        if (document.getClipId() == null) {
            document.setClipId(readId(source));
        }
    }

    private void normalizeChannel(ChannelDocument document, JsonNode source) {
        if (document.getChannelId() == null) {
            document.setChannelId(readId(source));
        }
    }

    private String readId(JsonNode source) {
        JsonNode id = source.get("id");
        return id == null || id.isNull() ? null : id.asText();
    }
}
