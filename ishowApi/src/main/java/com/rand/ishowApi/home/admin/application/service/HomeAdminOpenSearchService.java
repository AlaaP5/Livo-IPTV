package com.rand.ishowApi.home.admin.application.service;


import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.home.admin.domain.index.HomeIndex;
import com.rand.ishowApi.home.admin.domain.model.HomeBanner;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.shared.settings.ContentType;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeAdminOpenSearchService {
    private final OpenSearchClient openSearchClient;

    public void addBannerItem(HomeBanner banner) throws IOException {

        var searchResponse = openSearchClient.search(s -> s
                        .index(HomeIndex.HOME.getIndexName())
                        .query(q -> q
                                .bool(b -> b
                                        .must(m1 -> m1.term(t -> t.field("contentId").value(FieldValue.of((banner.getContentId())))))
                                        .must(m2 -> m2.term(t -> t.field("contentType.keyword").value(FieldValue.of(banner.getContentType().name()))))
                                )
                        )
                        .size(1)
                , HomeBanner.class);

        if (searchResponse.hits().hits().size()>0) {
            String docId = searchResponse.hits().hits().getFirst().id();
            HomeBanner doc = searchResponse.hits().hits().getFirst().source();
            openSearchClient.update(u -> u
                            .index(HomeIndex.HOME.getIndexName())
                            .id(docId)
                            .doc(doc),
                    HomeBanner.class
            );
        }
        else
        {
            openSearchClient.index(i -> i
                    .index(HomeIndex.HOME.getIndexName())
                    .document(banner)
            );
        }

    }

    public void removeBannerItem(String contentId, ContentType contentType) throws IOException {
        try{
            openSearchClient.deleteByQuery(b -> b
                    .index(HomeIndex.HOME.getIndexName())
                    .query(q -> q
                            .bool(bool -> bool
                                    .must(m1 -> m1
                                            .term(t -> t
                                                    .field("contentId")
                                                    .value(FieldValue.of(contentId))
                                            )
                                    )
                                    .must(m2 -> m2
                                            .term(t -> t
                                                    .field("contentType")
                                                    .value(FieldValue.of(contentType.getValue()))
                                            )
                                    )
                            )
                    )
            );
        }
        catch (Exception e){
            throw new CustomException(ApiResponseCode.ERROR_404, null);
        }
    }
    public List<HomeBanner> filterHomeBanner() throws IOException {
        SearchResponse<HomeBanner> response = openSearchClient.search(s -> s
                        .index(HomeIndex.HOME.getIndexName())
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m
                                                .matchAll(ma -> ma)
                                        )
                                )
                        ),
                HomeBanner.class
        );

        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();
    }


}
