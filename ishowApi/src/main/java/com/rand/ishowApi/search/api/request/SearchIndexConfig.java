package com.rand.ishowApi.search.api.request;


import com.rand.ishowApi.channel.admin.domain.openSearch.index.ChannelIndex;
import com.rand.ishowApi.clip.admin.domain.openSearch.index.ClipsIndex;
import com.rand.ishowApi.movie.admin.domain.openSearch.index.MoviesIndex;
import com.rand.ishowApi.series.admin.domain.openSearch.index.SeriesIndex;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.index.TvProgramIndex;

import java.util.List;

public record SearchIndexConfig(
        SearchIndex searchIndex,
        String indexName,
        boolean supportsRating,
        boolean supportsReleaseYear
) {

    public static final List<SearchIndexConfig> SEARCH_INDICES = List.of(
            new SearchIndexConfig(SearchIndex.MOVIES, MoviesIndex.MOVIES_SEARCH.getIndexName(), true, true),
            new SearchIndexConfig(SearchIndex.SERIES, SeriesIndex.SERIES_SEARCH.getIndexName(), true, true),
            new SearchIndexConfig(SearchIndex.TV_PROGRAM, TvProgramIndex.TV_PROGRAM_SEARCH.getIndexName(), false, true),
            new SearchIndexConfig(SearchIndex.CLIPS, ClipsIndex.CLIPS_SEARCH.getIndexName(), false, false),
            new SearchIndexConfig(SearchIndex.CHANNELS, ChannelIndex.CHANNEL_SEARCH.getIndexName(), false, false)
    );
}
