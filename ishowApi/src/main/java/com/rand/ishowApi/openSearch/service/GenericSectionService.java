package com.rand.ishowApi.openSearch.service;


import com.rand.ishowApi.openSearch.mapper.SectionAggregationMapper;
import com.rand.ishowApi.openSearch.mapper.SectionContentMapper;
import com.rand.ishowApi.openSearch.model.SectionDocument;
import com.rand.ishowApi.openSearch.response.SectionBannerResponse;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.shared.settings.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch.OpenSearchClient;

import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.UpdateByQueryRequest;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenericSectionService {
    private final OpenSearchClient openSearchClient;
        private final SectionAggregationMapper aggregationMapper;


    public void updateSectionData(
            String indexName,
            Section section
    ) throws IOException {

        UpdateByQueryRequest request = new UpdateByQueryRequest.Builder()
                .index(indexName)
                .query(q -> q
                        .term(t -> t
                                .field("sectionId")
                                .value(FieldValue.of(section.getId()))
                        )
                )
                .script(s -> s
                        .inline(i -> i
                                .source("""
                                    ctx._source.sectionTitleEn = params.sectionTitleEn;
                                    ctx._source.sectionTitleAr = params.sectionTitleAr;
                                    ctx._source.sectionOrder = params.sectionOrder;
                                    ctx._source.sectionActive = params.sectionActive;
                                    """)
                                .params("sectionTitleEn", JsonData.of(section.getTitleEn()))
                                .params("sectionTitleAr", JsonData.of(section.getTitleAr()))
                                .params("sectionOrder", JsonData.of(section.getOrder()))
                                .params("sectionActive", JsonData.of(section.getActive()))
                        )
                )

                .build();

        openSearchClient.updateByQuery(request);
    }

    public <D extends SectionDocument, R> SectionBannerResponse<R> getSections(
            String indexName,
            Class<D> documentClass,
            ContentType type,
            SectionContentMapper<D, R> mapper,
            boolean rightsRequired
    ) throws IOException {

            SearchResponse<D> response = openSearchClient.search(s -> s
                            .index(indexName)
                            .size(0)
                            .query(q -> q.bool(b -> {

                                b.filter(f -> f.term(t -> t.field("sectionActive").value(FieldValue.of(true))))
                                        .filter(f -> f.term(t -> t.field("sectionPublish").value(FieldValue.of(true))))
                                        .filter(f -> f.term(t -> t.field("isTop").value(FieldValue.of(true))))
                                        .filter(f -> f.term(t -> t.field("isPublish").value(FieldValue.of(true))))
                                        .filter(f -> f.term(t -> t.field("active").value(FieldValue.of(true))));

                                if (rightsRequired) {
                                    b.filter(f -> f.term(t -> t.field("hasRight").value(FieldValue.of(true))));
                                }

                                return b;
                            }))

                            // ✅ SECTIONS
                            .aggregations("sections", a -> a
                                    .terms(t -> t
                                            .field("sectionId")
                                            .order(List.of(Map.of("section_order", SortOrder.Asc)))
                                    )
                                    .aggregations("section_order", sub -> sub
                                            .min(m -> m.field("sectionOrder"))
                                    )
                                    .aggregations("section_name", sub -> sub
                                            .topHits(th -> th
                                                    .size(1)
                                                    .source(src -> src.filter(f -> f
                                                            .includes("sectionTitleEn", "sectionTitleAr")
                                                    ))
                                            )
                                    )
                                    .aggregations("items", sub -> sub
                                            .topHits(th -> th
                                                    .size(50)
                                                    .sort(so -> so.field(f -> f
                                                            .field("createDate")
                                                            .order(SortOrder.Desc)
                                                    ))
                                            )
                                    )
                            )

                            // ✅ BANNER (SEPARATE)
                            .aggregations("banner", a -> a
                                    .filter(f -> f.term(t -> t
                                            .field("sectionTitleEn")
                                            .value(FieldValue.of("banner"))
                                    ))
                                    .aggregations("items", sub -> sub
                                            .topHits(th -> th
                                                    .size(10)
                                                    .sort(so -> so.field(f -> f
                                                            .field("createDate")
                                                            .order(SortOrder.Desc)
                                                    ))
                                            )
                                    )
                            ),

                    documentClass
            );

            return aggregationMapper.map(response, documentClass, mapper, type);
        }
    public <D, R> List<R> getSectionContent(
            String indexName,
            Class<D> documentClass,
            SectionContentMapper<D, R> mapper,
            Long sectionId,
            boolean rightsRequired
    ) throws IOException {

        SearchResponse<D> response = openSearchClient.search(s -> s
                        .index(indexName)

                        .size(50)

                        .query(q -> q.bool(b -> {

                            b.filter(f -> f.term(t -> t
                                    .field("sectionId")
                                    .value(FieldValue.of(sectionId))
                            ));

                            b.filter(f -> f.term(t -> t
                                    .field("active")
                                    .value(FieldValue.of(true))
                            ));

                            b.filter(f -> f.term(t -> t
                                    .field("isPublish")
                                    .value(FieldValue.of(true))
                            ));

                            if (rightsRequired) {
                                b.filter(f -> f.term(t -> t
                                        .field("hasRight")
                                        .value(FieldValue.of(true))
                                ));
                            }

                            return b;
                        }))

                        // 🔥 SORT
                        .sort(so -> so.field(f -> f
                                .field("createDate")
                                .order(SortOrder.Desc)
                        )),
                documentClass
        );

        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();

        List<R> result = new ArrayList<>();

        if (response.hits() == null) {
            return result;
        }

        for (Hit<D> hit : response.hits().hits()) {

            D doc = hit.source();
            if (doc == null) continue;

            result.add(mapper.map(doc, lang));
        }

        return result;
    }

 public <T> String findDocId(String index, String field, Object value, Class<T> clazz) throws IOException {
    SearchResponse<T> response =
            openSearchClient.search(s -> s
                            .index(index)
                            .query(q -> q
                                    .term(t -> t
                                            .field(field)
                                            .value(FieldValue.of(value.toString()))
                                    )
                            )
                            .size(1),
                    clazz
            );

    return response.hits().hits().isEmpty()
            ? null
            : response.hits().hits().getFirst().id();
}

}
