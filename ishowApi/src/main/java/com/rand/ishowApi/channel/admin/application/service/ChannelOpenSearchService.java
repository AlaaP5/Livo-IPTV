package com.rand.ishowApi.channel.admin.application.service;

import com.rand.ishowApi.channel.admin.api.response.ChannelAdminSectionResponse;
import com.rand.ishowApi.channel.admin.application.manager.ChannelOpenSearchManager;
import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.channel.admin.domain.openSearch.index.ChannelIndex;
import com.rand.ishowApi.channel.admin.domain.openSearch.model.ChannelDocument;
import com.rand.ishowApi.channel.admin.domain.openSearch.model.ChannelSearchDocument;
import com.rand.ishowApi.channel.admin.mapper.ChannelSectionMapper;
import com.rand.ishowApi.exception.model.CustomException;
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
public class ChannelOpenSearchService {

    private final OpenSearchClient openSearchClient;
    private final ChannelOpenSearchManager manager;
    private final ChannelSectionMapper mapper;
    private final com.rand.ishowApi.channel.admin.mapper.ChannelSearchMapper searchMapper;

    // ==================== Update Channel Indexes ====================

    public void updateChannelIndexes(Channel channel) throws IOException {
        updateAllSectionIndexes(channel);
        updateSearchIndex(channel);
    }

    public void updateAllSectionIndexes(Channel channel) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (ChannelIndex index : ChannelIndex.getSectionIndexes()) {
            futures.add(updateChannelSectionIndex(channel, index.getIndexName()));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @Async
    public CompletableFuture<Void> updateChannelSectionIndex(Channel channel, String indexName) {
        try {
            SearchResponse<ChannelDocument> response = openSearchClient.search(s -> s
                            .index(indexName)
                            .query(q -> q.term(t -> t
                                    .field("channelId")
                                    .value(FieldValue.of(channel.getId()))
                            )),
                    ChannelDocument.class);

            for (Hit<ChannelDocument> hit : response.hits().hits()) {
                ChannelDocument doc = hit.source();
                if (doc == null) {
                    continue;
                }

                manager.updateChannelSectionDocument(doc, channel);

                openSearchClient.update(u -> u
                                .index(indexName)
                                .id(hit.id())
                                .doc(doc),
                        ChannelDocument.class);
            }

            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.CHANNEL_SECTION_INDEX_UPDATE_FAILED, null);
        }
    }

    public void updateSearchIndex(Channel channel) throws IOException {
        try {
            ChannelSearchDocument document = searchMapper.toDoc(channel);

            openSearchClient.index(i -> i
                    .index(ChannelIndex.CHANNEL_SEARCH.getIndexName())
                    .id(document.getId())
                    .document(document)
            );
        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.CHANNEL_SEARCH_INDEX_UPDATE_FAILED, null);
        }
    }

    // ==================== Channel Section Management ====================

    public List<ChannelAdminSectionResponse> getChannelsSection(Long sectionId, String indexName, String isTop, int page, int size) throws IOException {
        page = page - 1;
        int from = page * size;

        SearchResponse<ChannelDocument> response = openSearchClient.search(s -> {
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
                ChannelDocument.class);

        List<ChannelDocument> result = response.hits().hits().stream()
                .map(Hit::source)
                .toList();

        return mapper.toChannelSectionResponseList(result);
    }

    public void addChannelToSection(Channel channel, Section section, String indexName, String isTop) throws IOException {
        SearchResponse<ChannelDocument> searchResponse = openSearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q.bool(b -> b
                                .must(m1 -> m1.term(t -> t.field("sectionId").value(FieldValue.of(section.getId()))))
                                .must(m2 -> m2.term(t -> t.field("channelId").value(FieldValue.of(channel.getId()))))
                        ))
                        .size(1),
                ChannelDocument.class);

        if (!searchResponse.hits().hits().isEmpty()) {
            Hit<ChannelDocument> hit = searchResponse.hits().hits().getFirst();
            ChannelDocument doc = hit.source();
            if (doc == null) {
                throw new CustomException(ApiResponseCode.CHANNEL_NOT_FOUND, null);
            }

            manager.updateChannelSectionDocument(doc, channel);
            manager.setIsTop(doc, isTop);

            openSearchClient.update(u -> u
                            .index(indexName)
                            .id(hit.id())
                            .doc(doc),
                    ChannelDocument.class);
        } else {
            ChannelDocument channelSectionDocument = manager.createChannelSectionDocument(channel, section, isTop);
            openSearchClient.index(i -> i
                    .index(indexName)
                    .document(channelSectionDocument)
            );
        }
    }

    public void removeChannelFromSection(Long sectionId, String channelId, String indexName) {
        try {
            openSearchClient.deleteByQuery(b -> b
                    .index(indexName)
                    .query(q -> q.bool(bool -> bool
                            .must(m1 -> m1.term(t -> t.field("sectionId").value(FieldValue.of(sectionId))))
                            .must(m2 -> m2.term(t -> t.field("channelId").value(FieldValue.of(channelId))))
                    ))
            );
        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.CHANNEL_DELETE_FROM_SECTION_FAILED, null);
        }
    }

    public void updateChannelIsTop(Long sectionId, String channelId, String indexName, String isTop) throws IOException {
        SearchResponse<ChannelDocument> searchResponse = openSearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q.bool(b -> b
                                .must(m1 -> m1.term(t -> t.field("sectionId").value(FieldValue.of(sectionId))))
                                .must(m2 -> m2.term(t -> t.field("channelId").value(FieldValue.of(channelId))))
                        ))
                        .size(1),
                ChannelDocument.class);

        if (searchResponse.hits().hits().isEmpty()) {
            throw new CustomException(ApiResponseCode.CHANNEL_NOT_FOUND, null);
        }

        Hit<ChannelDocument> hit = searchResponse.hits().hits().getFirst();
        ChannelDocument doc = hit.source();
        if (doc == null) {
            throw new CustomException(ApiResponseCode.CHANNEL_NOT_FOUND, null);
        }

        manager.setIsTop(doc, isTop);
        openSearchClient.update(u -> u
                        .index(indexName)
                        .id(hit.id())
                        .doc(doc),
                ChannelDocument.class);
    }
}


