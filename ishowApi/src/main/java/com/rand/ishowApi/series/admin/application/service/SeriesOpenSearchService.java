package com.rand.ishowApi.series.admin.application.service;

import com.rand.ishowApi.exception.model.CustomException;

import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.series.admin.api.response.SeriesSectionAdminResponse;
import com.rand.ishowApi.series.admin.application.manager.SeriesOpenSearchManager;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.openSearch.index.SeriesIndex;
import com.rand.ishowApi.series.admin.domain.openSearch.model.SeriesDocument;
import com.rand.ishowApi.series.admin.domain.openSearch.model.SeriesSearchDocument;
import com.rand.ishowApi.series.admin.mapper.SeriesAdminMapper;
import com.rand.ishowApi.series.admin.mapper.SeriesSearchMapper;
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
public class SeriesOpenSearchService {

    private final OpenSearchClient openSearchClient;
    private final SeriesOpenSearchManager manager;
    private final SeriesAdminMapper mapper;
    private final SeriesSearchMapper searchMapper;


    public void updateSeriesIndexes(Series series) throws IOException {
        updateSeriesSearchIndex(series);
        updateAllSectionIndexes(series);
    }
    public void updateSeriesSearchIndex(Series series){
        try {
            // Map entity to search document and index
            SeriesSearchDocument document = searchMapper.toDoc(series);

            openSearchClient.index(i -> i
                    .index(SeriesIndex.SERIES_SEARCH.getIndexName())
                    .id(document.getId())
                    .document(document)
            );

        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.SERIES_SEARCH_INDEX_NOT_FOUND, null);
        }

    }



    public void updateAllSectionIndexes(Series series) {

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (SeriesIndex index : SeriesIndex.getSectionIndexes()) {

            futures.add(
                    updateSeriesSectionIndex(
                            series,
                            index.getIndexName()
                    )
            );
        }
        CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        ).join();
    }


    @Async
    public CompletableFuture<Void> updateSeriesSectionIndex(
            Series series,
            String indexName
    ) {

        try {

            SearchResponse<SeriesDocument> response =
                    openSearchClient.search(s -> s
                                    .index(indexName)
                                    .query(q -> q
                                            .term(t -> t
                                                    .field("seriesId")
                                                    .value(FieldValue.of(series.getId()))
                                            )
                                    ),
                            SeriesDocument.class
                    );

            for (Hit<SeriesDocument> hit : response.hits().hits()) {

                String docId = hit.id();
                SeriesDocument doc = hit.source();

                if (doc == null) {
                    continue;
                }

                manager.updateSeriesSectionDocument(doc, series);

                openSearchClient.update(u -> u
                                .index(indexName)
                                .id(docId)
                                .doc(doc),
                        SeriesDocument.class
                );
            }

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            throw new CustomException(
                    ApiResponseCode.SERIES_SECTION_INDEX_UPDATE_FAILED,
                    null
            );
        }
    }




    public List<SeriesSectionAdminResponse> getSeriesSection(Long sectionId,String indexName,  String isTop, int page, int size)throws IOException
    {
        page = page-1;
        int from = page * size;

        SearchResponse<SeriesDocument> response = openSearchClient.search(s -> {
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
                SeriesDocument.class
        );

        List<SeriesDocument> result = response.hits()
                .hits()
                .stream()
                .map(Hit::source)
                .toList();

        return mapper.toSeriesSectionResponseList(result);
    }
          
    public void removeSeriesFromSection(Long sectionId, String seriesId ,String indexName)throws IOException {
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
                                                    .field("seriesId")
                                                    .value(FieldValue.of(seriesId))
                                            )
                                    )
                            )
                    )
            );
        } catch (Exception e){
            throw new CustomException(ApiResponseCode.SERIES_DELETE_FROM_SECTION_FAILED, null);
        }
    }




    public void addSeriesToSection(Series series,Section section,String indexName,  String isTop) throws IOException {
     if (!series.getIsPublish())
        {
            throw new CustomException(ApiResponseCode.SERIES_IS_NOT_PUBLISH, null);
        }
        var searchResponse = openSearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .must(m1 -> m1.term(t -> t.field("sectionId").value(FieldValue.of(section.getId()))))
                                        .must(m2 -> m2.term(t -> t.field("seriesId").value(FieldValue.of(series.getId()))))
                                )
                        )
                        .size(1)
                , SeriesDocument.class);

        if (searchResponse.hits().hits().size()>0) {
            String docId = searchResponse.hits().hits().getFirst().id();

            SeriesDocument doc = searchResponse.hits().hits().getFirst().source();

            if(doc == null)
                throw new CustomException(ApiResponseCode.SERIES_NOT_FOUND, null);

            manager.updateSeriesSectionDocument(doc,series);
            manager.setIsTop(doc,isTop);
            openSearchClient.update(u -> u
                            .index(indexName)
                            .id(docId)
                            .doc(doc),
                    SeriesDocument.class
            );
        }
        else
        {

            SeriesDocument seriesSectionDocument = manager.createSeriesSectionDocument(series, section,isTop);
            openSearchClient.index(i -> i
                    .index(indexName)
                    .document(seriesSectionDocument)
            );

        }

    }


    public void updateSeriesIsTop(Long sectionId,String seriesId ,String indexName,  String isTop) throws IOException {

        var searchResponse = openSearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .must(m1 -> m1.term(t -> t.field("sectionId").value(FieldValue.of(sectionId))))
                                        .must(m2 -> m2.term(t -> t.field("seriesId").value(FieldValue.of(seriesId))))
                                )
                        )
                        .size(1)
                , SeriesDocument.class);

        if (searchResponse.hits().hits().isEmpty()) {
            throw new CustomException(ApiResponseCode.SERIES_NOT_FOUND, null);
        }

        var hit = searchResponse.hits().hits().getFirst();

        String docId = hit.id();
        SeriesDocument doc = hit.source();

        // update field
        manager.setIsTop(doc, isTop);

        openSearchClient.update(u -> u
                        .index(indexName)
                        .id(docId)
                        .doc(doc),
                SeriesDocument.class
        );
    }

}
