package com.rand.ishowApi.home.mobile.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.rand.ishowApi.channel.admin.domain.openSearch.index.ChannelIndex;
import com.rand.ishowApi.clip.admin.domain.openSearch.index.ClipsIndex;
import com.rand.ishowApi.home.admin.domain.index.HomeIndex;
import com.rand.ishowApi.home.mobile.api.response.HomeMobileResponse;
import com.rand.ishowApi.home.mobile.mapper.HomeMobileMapper;
import com.rand.ishowApi.movie.admin.domain.openSearch.index.MoviesIndex;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.series.admin.domain.openSearch.index.SeriesIndex;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.index.TvProgramIndex;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch.core.MsearchResponse;
import org.opensearch.client.opensearch.core.msearch.RequestItem;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class HomeMobileService {
private final OpenSearchClient openSearchClient;
private final HomeMobileMapper homeMobileMapper;

    public HomeMobileResponse getHomeMobile() throws IOException {

        boolean rightsRequired = true;
       String lang= String.valueOf(DeviceContext.get().getLanguage());

        MsearchResponse<JsonNode> response = openSearchClient.msearch(ms -> ms
                        .searches(
                                Arrays.asList(

                                        // HOME
                                        RequestItem.of(r -> r
                                                .header(h -> h.index(HomeIndex.HOME.getIndexName()))
                                                .body(b -> b
                                                        .query(q -> q
                                                                .matchAll(ma -> ma)
                                                        )
                                                )
                                        ),

                                        // MOVIES
                                        RequestItem.of(r -> r
                                                .header(h -> h.index(MoviesIndex.MOVIES_HOME.getIndexName()))
                                                .body(b -> b
                                                        .query(q -> q
                                                                .bool(buildCommonFilter(rightsRequired))
                                                        )
                                                )
                                        ),

                                        // SERIES
                                        RequestItem.of(r -> r
                                                .header(h -> h.index(SeriesIndex.SERIES_HOME.getIndexName()))
                                                .body(b -> b
                                                        .query(q -> q
                                                                .bool(buildCommonFilter(rightsRequired))
                                                        )
                                                )
                                        ),

                                        // TV PROGRAM
                                        RequestItem.of(r -> r
                                                .header(h -> h.index(TvProgramIndex.TV_PROGRAM_HOME.getIndexName()))
                                                .body(b -> b
                                                        .query(q -> q
                                                                .bool(buildCommonFilter(rightsRequired))
                                                        )
                                                )
                                        ),

                                        // CLIPS
                                        RequestItem.of(r -> r
                                                .header(h -> h.index(ClipsIndex.CLIPS_HOME.getIndexName()))
                                                .body(b -> b
                                                        .query(q -> q
                                                                .bool(buildCommonFilter(rightsRequired))
                                                        )
                                                )
                                        ),

                                        // CHANNEL
                                        RequestItem.of(r -> r
                                                .header(h -> h.index(ChannelIndex.CHANNEL_HOME.getIndexName()))
                                                .body(b -> b
                                                        .query(q -> q
                                                                .bool(buildCommonFilter(rightsRequired))
                                                        )
                                                )
                                        )
                                )
                        ),
                JsonNode.class
        );


        return homeMobileMapper.maptoHomeMobileResponse(response,lang);
    }
    private BoolQuery buildCommonFilter(boolean rightsRequired) {

        BoolQuery.Builder b = new BoolQuery.Builder();

        b.filter(f -> f.term(t -> t.field("sectionActive").value(FieldValue.of(true))));
        b.filter(f -> f.term(t -> t.field("sectionPublish").value(FieldValue.of(true))));
        b.filter(f -> f.term(t -> t.field("isTop").value(FieldValue.of(true))));
        b.filter(f -> f.term(t -> t.field("isPublish").value(FieldValue.of(true))));
        b.filter(f -> f.term(t -> t.field("active").value(FieldValue.of(true))));

        if (rightsRequired) {
            b.filter(f -> f.term(t -> t.field("hasRight").value(FieldValue.of(true))));
        }
        return b.build();
    }


}
