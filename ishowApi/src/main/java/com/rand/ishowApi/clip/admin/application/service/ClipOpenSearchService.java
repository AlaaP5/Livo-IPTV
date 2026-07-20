package com.rand.ishowApi.clip.admin.application.service;

import com.rand.ishowApi.clip.admin.api.response.ClipSectionAdminResponse;
import com.rand.ishowApi.clip.admin.application.manager.ClipOpenSearchManager;
import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.clip.admin.domain.openSearch.index.ClipsIndex;
import com.rand.ishowApi.clip.admin.domain.openSearch.model.ClipDocument;
import com.rand.ishowApi.clip.admin.domain.openSearch.model.ClipSearchDocument;
import com.rand.ishowApi.clip.admin.mapper.ClipSectionMapper;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.section.domain.entity.Section;
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
public class ClipOpenSearchService {

    private final OpenSearchClient openSearchClient;
    private final ClipOpenSearchManager manager;
    private final ClipSectionMapper mapper;
    private final com.rand.ishowApi.clip.admin.mapper.ClipSearchMapper searchMapper;

    public void updateClipIndexes(Clip clip) throws IOException {
        updateAllSectionIndexes(clip);
        updateSearchIndex(clip);
    }

    public void updateAllSectionIndexes(Clip clip) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (ClipsIndex index : ClipsIndex.getSectionIndexes()) {
            futures.add(updateClipSectionIndex(clip, index.getIndexName()));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @Async
    public CompletableFuture<Void> updateClipSectionIndex(Clip clip, String indexName) {
        try {
            SearchResponse<ClipDocument> response = openSearchClient.search(s -> s
                            .index(indexName)
                            .query(q -> q.term(t -> t
                                    .field("clipId")
                                    .value(FieldValue.of(clip.getId()))
                            )),
                    ClipDocument.class);

            for (Hit<ClipDocument> hit : response.hits().hits()) {
                ClipDocument doc = hit.source();
                if (doc == null) {
                    continue;
                }

                manager.updateClipSectionDocument(doc, clip);

                openSearchClient.update(u -> u
                                .index(indexName)
                                .id(hit.id())
                                .doc(doc),
                        ClipDocument.class);
            }

            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.CLIP_SECTION_INDEX_UPDATE_FAILED, null);
        }
    }

    public void updateSearchIndex(Clip clip) {
        try {
            ClipSearchDocument document = searchMapper.toDoc(clip);

            openSearchClient.index(i -> i
                    .index(ClipsIndex.CLIPS_SEARCH.getIndexName())
                    .id(document.getId())
                    .document(document)
            );
        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.CLIP_SEARCH_INDEX_UPDATE_FAILED, null);
        }
    }

    public List<ClipSectionAdminResponse> getClipsSection(Long sectionId, String indexName, String isTop, int page, int size) throws IOException {
        page = page - 1;
        int from = page * size;

        SearchResponse<ClipDocument> response = openSearchClient.search(s -> {
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
                ClipDocument.class);

        List<ClipDocument> result = response.hits().hits().stream()
                .map(Hit::source)
                .toList();

        return mapper.toClipSectionResponseList(result);
    }

    public void addClipToSection(Clip clip, Section section, String indexName, String isTop) throws IOException {
        if (clip.getTranscodeStatus() != TranscodeStatus.COMPLETED) {
            throw new CustomException(ApiResponseCode.CLIP_TRANSCODE_NOT_COMPLETE, null);
        }

        if (!clip.getIsPublish()) {
            throw new CustomException(ApiResponseCode.CLIP_NOT_PUBLISH, null);
        }

        SearchResponse<ClipDocument> searchResponse = openSearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q.bool(b -> b
                                .must(m1 -> m1.term(t -> t.field("sectionId").value(FieldValue.of(section.getId()))))
                                .must(m2 -> m2.term(t -> t.field("clipId").value(FieldValue.of(clip.getId()))))
                        ))
                        .size(1),
                ClipDocument.class);

        if (!searchResponse.hits().hits().isEmpty()) {
            Hit<ClipDocument> hit = searchResponse.hits().hits().getFirst();
            ClipDocument doc = hit.source();
            if (doc == null) {
                throw new CustomException(ApiResponseCode.CLIP_NOT_FOUND, null);
            }

            manager.updateClipSectionDocument(doc, clip);
            manager.setIsTop(doc, isTop);

            openSearchClient.update(u -> u
                            .index(indexName)
                            .id(hit.id())
                            .doc(doc),
                    ClipDocument.class);
        } else {
            ClipDocument clipSectionDocument = manager.createClipSectionDocument(clip, section, isTop);
            openSearchClient.index(i -> i
                    .index(indexName)
                    .document(clipSectionDocument)
            );
        }
    }

    public void removeClipFromSection(Long sectionId, String clipId, String indexName) {
        try {
            openSearchClient.deleteByQuery(b -> b
                    .index(indexName)
                    .query(q -> q
                            .bool(bool -> bool
                                    .must(m1 -> m1.term(t -> t.field("sectionId").value(FieldValue.of(sectionId))))
                                    .must(m2 -> m2.term(t -> t.field("clipId").value(FieldValue.of(clipId))))
                            )
                    )
            );
        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.CLIP_DELETE_FROM_SECTION_FAILED, null);
        }
    }

    public void updateClipIsTop(Long sectionId, String clipId, String indexName, String isTop) throws IOException {
        SearchResponse<ClipDocument> searchResponse = openSearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q.bool(b -> b
                                .must(m1 -> m1.term(t -> t.field("sectionId").value(FieldValue.of(sectionId))))
                                .must(m2 -> m2.term(t -> t.field("clipId").value(FieldValue.of(clipId))))
                        ))
                        .size(1),
                ClipDocument.class);

        if (searchResponse.hits().hits().isEmpty()) {
            throw new CustomException(ApiResponseCode.CLIP_NOT_FOUND, null);
        }

        Hit<ClipDocument> hit = searchResponse.hits().hits().getFirst();
        ClipDocument doc = hit.source();
        if (doc == null) {
            throw new CustomException(ApiResponseCode.CLIP_NOT_FOUND, null);
        }

        manager.setIsTop(doc, isTop);
        openSearchClient.update(u -> u
                        .index(indexName)
                        .id(hit.id())
                        .doc(doc),
                ClipDocument.class);
    }
}


