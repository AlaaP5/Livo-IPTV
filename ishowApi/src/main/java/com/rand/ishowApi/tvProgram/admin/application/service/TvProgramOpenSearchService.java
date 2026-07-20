package com.rand.ishowApi.tvProgram.admin.application.service;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.series.admin.domain.openSearch.index.SeriesIndex;
import com.rand.ishowApi.tvProgram.admin.api.response.TvProgramSectionAdminResponse;
import com.rand.ishowApi.tvProgram.admin.application.manager.TvProgramOpenSearchManager;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.index.TvProgramIndex;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.model.TvProgramDocument;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.model.TvProgramSearchDocument;
import com.rand.ishowApi.tvProgram.admin.mapper.TvProgramAdminMapper;
import com.rand.ishowApi.tvProgram.admin.mapper.TvProgramSearchMapper;
import com.rand.ishowApi.utils.BooleanConverter;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TvProgramOpenSearchService {

    private final OpenSearchClient openSearchClient;
    private final TvProgramOpenSearchManager manager;
    private final TvProgramAdminMapper mapper;
    private final TvProgramSearchMapper searchMapper;

    public void updateTvProgramIndexes(TvProgram tvProgram) {
        updateTvProgramSearchIndex(tvProgram);
        updateAllSectionIndexes(tvProgram);
    }

    public void updateTvProgramSearchIndex(TvProgram tvProgram) {
        try {
            TvProgramSearchDocument document = searchMapper.toDoc(tvProgram);

            openSearchClient.index(i -> i
                    .index(TvProgramIndex.TV_PROGRAM_SEARCH.getIndexName())
                    .id(document.getId())
                    .document(document)
            );

        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.TV_PROGRAM_SEARCH_INDEX_NOT_FOUND, null);
        }
    }

    public void updateAllSectionIndexes(TvProgram tvProgram) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (TvProgramIndex index : TvProgramIndex.getSectionIndexes()) {

            futures.add(
                    updateTvProgramSectionIndex(
                            tvProgram,
                            index.getIndexName()
                    )
            );
        }
        CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        ).join();
    }


    @Async
    public CompletableFuture<Void> updateTvProgramSectionIndex(TvProgram tvProgram, String indexName) {
        try {
            SearchResponse<TvProgramDocument> response =
                    openSearchClient.search(s -> s
                                    .index(indexName)
                                    .query(q -> q
                                            .term(t -> t
                                                    .field("tvProgramId")
                                                    .value(FieldValue.of(tvProgram.getId()))
                                            )
                                    ),
                            TvProgramDocument.class
                    );

            for (Hit<TvProgramDocument> hit : response.hits().hits()) {
                String docId = hit.id();
                TvProgramDocument doc = hit.source();

                if (doc == null) {
                    continue;
                }

                manager.updateTvProgramSectionDocument(doc, tvProgram);

                openSearchClient.update(u -> u
                                .index(indexName)
                                .id(docId)
                                .doc(doc),
                        TvProgramDocument.class
                );
            }

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.TV_PROGRAM_SECTION_INDEX_UPDATE_FAILED, null);
        }
    }

    public List<TvProgramSectionAdminResponse> getTvProgramSection(
            Long sectionId,
            String indexName,
            String isTop,
            int page,
            int size
    ) throws IOException {
        page = page - 1;
        int from = page * size;

        SearchResponse<TvProgramDocument> response = openSearchClient.search(s -> {
                    BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

                    boolBuilder.must(m -> m.term(t -> t
                            .field("sectionId")
                            .value(FieldValue.of(sectionId))
                    ));

                    if (isTop != null) {
                        boolBuilder.must(m -> m.term(t -> t
                                .field("isTop")
                                .value(FieldValue.of(BooleanConverter.getActiveBoolean(isTop)))
                        ));
                    }

                    return s.index(indexName)
                            .query(q -> q.bool(boolBuilder.build()))
                            .from(from)
                            .size(size);
                },
                TvProgramDocument.class
        );

        List<TvProgramDocument> result = response.hits()
                .hits()
                .stream()
                .map(Hit::source)
                .toList();

        return result.stream().map(mapper::toTvProgramSectionResponse).toList();
    }

    public void removeTvProgramFromSection(Long sectionId, String tvProgramId, String indexName) {
        try {
            openSearchClient.deleteByQuery(b -> b
                    .index(indexName)
                    .query(q -> q
                            .bool(bool -> bool
                                    .must(m1 -> m1
                                            .term(t -> t
                                                    .field("sectionId")
                                                    .value(FieldValue.of(sectionId))
                                            )
                                    )
                                    .must(m2 -> m2
                                            .term(t -> t
                                                    .field("tvProgramId")
                                                    .value(FieldValue.of(tvProgramId))
                                            )
                                    )
                            )
                    )
            );
        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.TV_PROGRAM_DELETE_FROM_SECTION_FAILED, null);
        }
    }

    public void addTvProgramToSection(TvProgram tvProgram, Section section, String indexName, String isTop) throws IOException {
        if (!tvProgram.getIsPublish()) {
            throw new CustomException(ApiResponseCode.TV_PROGRAM_IS_NOT_PUBLISH, null);
        }

        var searchResponse = openSearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .must(m1 -> m1.term(t -> t.field("sectionId").value(FieldValue.of(section.getId()))))
                                        .must(m2 -> m2.term(t -> t.field("tvProgramId").value(FieldValue.of(tvProgram.getId()))))
                                )
                        )
                        .size(1)
                , TvProgramDocument.class);

        if (!searchResponse.hits().hits().isEmpty()) {
            String docId = searchResponse.hits().hits().getFirst().id();
            TvProgramDocument doc = searchResponse.hits().hits().getFirst().source();

            if (doc == null) {
                throw new CustomException(ApiResponseCode.TV_PROGRAM_NOT_FOUND, null);
            }

            manager.updateTvProgramSectionDocument(doc, tvProgram);
            manager.setIsTop(doc, isTop);
            openSearchClient.update(u -> u
                            .index(indexName)
                            .id(docId)
                            .doc(doc),
                    TvProgramDocument.class
            );
        } else {
            TvProgramDocument document = manager.createTvProgramSectionDocument(tvProgram, section, isTop);
            openSearchClient.index(i -> i
                    .index(indexName)
                    .document(document)
            );
        }
    }

    public void updateTvProgramIsTop(Long sectionId, String tvProgramId, String indexName, String isTop) throws IOException {
        var searchResponse = openSearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .must(m1 -> m1.term(t -> t.field("sectionId").value(FieldValue.of(sectionId))))
                                        .must(m2 -> m2.term(t -> t.field("tvProgramId").value(FieldValue.of(tvProgramId))))
                                )
                        )
                        .size(1)
                , TvProgramDocument.class);

        if (searchResponse.hits().hits().isEmpty()) {
            throw new CustomException(ApiResponseCode.TV_PROGRAM_NOT_FOUND, null);
        }

        var hit = searchResponse.hits().hits().getFirst();
        String docId = hit.id();
        TvProgramDocument doc = hit.source();

        if (doc == null) {
            throw new CustomException(ApiResponseCode.TV_PROGRAM_NOT_FOUND, null);
        }

        manager.setIsTop(doc, isTop);

        openSearchClient.update(u -> u
                        .index(indexName)
                        .id(docId)
                        .doc(doc),
                TvProgramDocument.class
        );
    }
}


