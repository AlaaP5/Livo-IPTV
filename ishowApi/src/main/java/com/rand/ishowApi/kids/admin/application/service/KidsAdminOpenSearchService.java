package com.rand.ishowApi.kids.admin.application.service;


import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.kids.admin.domain.index.KidsIndex;
import com.rand.ishowApi.kids.admin.domain.model.KidsBanner;
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
public class KidsAdminOpenSearchService {
    private final OpenSearchClient openSearchClient;

    public void addBannerItem(KidsBanner banner) throws IOException {

        var searchResponse = openSearchClient.search(s -> s
                        .index(KidsIndex.KIDS.getIndexName())
                        .query(q -> q
                                .bool(b -> b
                                        .must(m1 -> m1.term(t -> t.field("contentId").value(FieldValue.of((banner.getContentId())))))
                                        .must(m2 -> m2.term(t -> t.field("contentType.keyword").value(FieldValue.of(banner.getContentType().name()))))
                                )
                        )
                        .size(1)
                , KidsBanner.class);

        if (searchResponse.hits().hits().size()>0) {
            String docId = searchResponse.hits().hits().getFirst().id();
            KidsBanner doc = searchResponse.hits().hits().getFirst().source();
            openSearchClient.update(u -> u
                            .index(KidsIndex.KIDS.getIndexName())
                            .id(docId)
                            .doc(doc),
                    KidsBanner.class
            );
        }
        else
        {
            openSearchClient.index(i -> i
                    .index(KidsIndex.KIDS.getIndexName())
                    .document(banner)
            );
        }

    }
    public void removeBannerItem(String contentId, ContentType contentType ) throws IOException {
        try{
            openSearchClient.deleteByQuery(b -> b
                    .index(KidsIndex.KIDS.getIndexName())
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

    public List<KidsBanner> filterHomeBanner() throws IOException {
        SearchResponse<KidsBanner> response = openSearchClient.search(s -> s
                        .index(KidsIndex.KIDS.getIndexName())
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m
                                                .matchAll(ma -> ma)
                                        )
                                )
                        ),
                KidsBanner.class
        );

        return response.hits().hits().stream()
                .map(Hit::source)
                .toList();
    }


}

