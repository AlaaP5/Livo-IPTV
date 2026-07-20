package com.rand.ishowApi.search.service;



import com.fasterxml.jackson.databind.JsonNode;
import com.rand.ishowApi.search.api.request.SearchIndex;
import com.rand.ishowApi.search.api.request.SearchIndexConfig;
import com.rand.ishowApi.search.api.request.SearchRequest;
import com.rand.ishowApi.search.api.response.SearchResponse;
import com.rand.ishowApi.search.mapper.SearchMapper;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.shared.language.Language;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch.core.MsearchResponse;
import org.opensearch.client.opensearch.core.msearch.RequestItem;
import org.springframework.stereotype.Service;
import org.opensearch.client.opensearch.core.msearch.MultisearchBody.Builder;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final OpenSearchClient openSearchClient;
    private final SearchMapper mapper;

    public SearchResponse search(SearchRequest request) throws IOException {
        boolean rightsRequired = true;
        String lang = String.valueOf(DeviceContext.get().getLanguage());
        //boolean allIndices = isAllIndex(request);
        int page =  resolvePage(request);
        int size =  resolveSize(request);

        MsearchResponse<JsonNode> response = openSearchClient.msearch(ms -> ms
                .searches(
                        SearchIndexConfig.SEARCH_INDICES.stream()
                                .map(config -> buildRequestItem(config, request, shouldSearchIndex(request, config), rightsRequired, page, size))
                                .toList()
                ),
                JsonNode.class
        );
        return mapper.toSearchResponse(response, lang);
    }

    private RequestItem buildRequestItem(
            SearchIndexConfig config,
            SearchRequest request,
            boolean shouldSearch,
            boolean rightsRequired,
            int page,
            int size
    ) {
        int from = (page - 1) * size;

        return RequestItem.of(r -> r
                .header(h -> h.index(config.indexName()))
                .body(b -> {
                    b.from(from)
                            .size(size)
                            .query(q -> shouldSearch
                                    ? q.bool(buildSearchQuery(request, config, rightsRequired))
                                    : q.matchNone(m -> m));

                    applySort(b, request, config);

                    return b;
                })
        );
    }

    private BoolQuery buildSearchQuery(SearchRequest request, SearchIndexConfig config, boolean rightsRequired) {
        BoolQuery.Builder bool = new BoolQuery.Builder()
                .filter(f -> f.term(t -> t.field("active").value(FieldValue.of(true))))
                .filter(f -> f.term(t -> t.field("isPublish").value(FieldValue.of(true))));

        if (rightsRequired) {
            bool.filter(f -> f.term(t -> t.field("hasRight").value(FieldValue.of(true))));
        }

        if (request != null && request.getText() != null && !request.getText().isBlank()) {

            String text = request.getText().toLowerCase().trim();

            bool.must(m -> m.bool(b -> b
                    // 1. Strong title match (ranking)
                    .should(s -> s.multiMatch(mm -> mm
                            .query(text)
                            .fields("titleEn^10", "titleAr^10")
                            .fuzziness("AUTO")
                    ))

                    // 2. Description fallback
                    .should(s -> s.multiMatch(mm -> mm
                            .query(text)
                            .fields("descriptionEn^2", "descriptionAr^2")
                    ))

                    // 3. PREFIX MATCH
                    .should(s -> s.prefix(p -> p
                            .field("titleEn")
                            .value(text)
                    ))
                    .should(s -> s.prefix(p -> p
                            .field("titleAr")
                            .value(text)
                    ))

                    // 4. WILDCARD fallback (VERY IMPORTANT for 2 chars)
                    .should(s -> s.wildcard(w -> w
                            .field("titleEn")
                            .value(text + "*")
                    ))
                    .should(s -> s.wildcard(w -> w
                            .field("titleAr")
                            .value(text + "*")
                    ))
                    .minimumShouldMatch("1")
            ));
        }

        if (request != null && request.getTags() != null && !request.getTags().isEmpty()) {
            List<FieldValue> tagIds = request.getTags().stream()
                    .map(FieldValue::of)
                    .toList();

            bool.filter(f -> f.terms(t -> t
                    .field("tags.id")
                    .terms(v -> v.value(tagIds))
            ));
        }

        if (request != null && request.getIsKids() != null) {
            bool.filter(f -> f.term(t -> t.field("isKids").value(FieldValue.of(request.getIsKids()))));
        }

        if (request != null && request.getIsSports() != null) {
            bool.filter(f -> f.term(t -> t.field("isSports").value(FieldValue.of(request.getIsSports()))));
        }

        if (request != null && request.getLanguage() != null) {
            bool.filter(f -> f.term(t -> t.field("language.keyword").value(FieldValue.of(request.getLanguage().name()))));
        }

        if (request != null && request.getRating() != null && config.supportsRating()) {
            bool.filter(f -> f.range(r -> r.field("rating").gte(JsonData.of(request.getRating()))));
        }

        if (request != null && request.getReleaseYear() != null && config.supportsReleaseYear()) {
            bool.filter(f -> f.term(t -> t.field("releaseYear").value(FieldValue.of(request.getReleaseYear()))));
        }

        return bool.build();
    }

    private void applySort(
            Builder builder,
            SearchRequest request,
            SearchIndexConfig config
    ) {
        SortOrder order = resolveSortOrder(request);
        if (order == null) {
            return;
        }

        String field = resolveSortField(config);
        if (field == null) {
            return;
        }

        builder.sort(so -> so.field(f -> f
                .field(field)
                .order(order)
        ));
    }

    private String resolveSortField(SearchIndexConfig config) {
        return switch (config.searchIndex()) {
            case MOVIES, SERIES -> "rating";
            case CLIPS -> "publishDate";
            case CHANNELS -> resolveChannelTitleSortField();
            default -> null;
        };
    }

    private String resolveChannelTitleSortField() {
        if (DeviceContext.exists() && DeviceContext.get().getLanguage() == Language.AR) {
            return "titleAr.keyword";
        }

        return "titleEn.keyword";
    }

    private boolean isAllIndex(SearchRequest request) {
        return request == null || request.getSearchIndex() == null || request.getSearchIndex() == SearchIndex.ALL;
    }

    private boolean shouldSearchIndex(SearchRequest request, SearchIndexConfig config) {
        if (isAllIndex(request)) {
            return true;
        }
        return request.getSearchIndex() == config.searchIndex();
    }

    private int resolvePage(SearchRequest request) {
        if (request == null || request.getPage() == null || request.getPage() < 1) {
            return 1;
        }
        return request.getPage();
    }

    private int resolveSize(SearchRequest request) {
        if (request == null || request.getSize() == null) {
            return 50;
        }
        return Math.min(Math.max(request.getSize(), 1), 50);
    }

    private SortOrder resolveSortOrder(SearchRequest request) {
        if (request == null || request.getSort() == null) {
            return null;
        }

        return switch (request.getSort()) {
            case asc -> SortOrder.Asc;
            case desc -> SortOrder.Desc;
        };
    }
}
