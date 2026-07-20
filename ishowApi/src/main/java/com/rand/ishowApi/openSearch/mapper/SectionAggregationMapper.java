package com.rand.ishowApi.openSearch.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.rand.ishowApi.openSearch.model.SectionDocument;
import com.rand.ishowApi.openSearch.response.SectionBannerResponse;
import com.rand.ishowApi.openSearch.response.SectionResponse;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.utils.LocalizedText;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch._types.aggregations.Aggregate;
import org.opensearch.client.opensearch._types.aggregations.LongTermsAggregate;
import org.opensearch.client.opensearch._types.aggregations.LongTermsBucket;
import org.opensearch.client.opensearch._types.aggregations.FilterAggregate;
import org.opensearch.client.opensearch.core.MsearchResponse;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@Component
@RequiredArgsConstructor
public class SectionAggregationMapper {

    private final JsonDataConverter converter;

    // ✅ FIX: MUST extend SectionDocument
    public <D extends SectionDocument, R> SectionBannerResponse<R> map(
            SearchResponse<D> response,
            Class<D> documentClass,
            SectionContentMapper<D, R> mapper,
            ContentType type
    ) {

        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();

        SectionBannerResponse<R> result = new SectionBannerResponse<>();

        result.setBanner(mapBanner(response, documentClass, mapper, lang));
        result.setSections(mapSections(response, documentClass, mapper, type, lang));

        return result;
    }

    // ---------------- BANNER ----------------
    private <D extends SectionDocument, R> List<R> mapBanner(
            SearchResponse<D> response,
            Class<D> documentClass,
            SectionContentMapper<D, R> mapper,
            String lang
    ) {

        if (response.aggregations() == null || !response.aggregations().containsKey("banner")) {
            return Collections.emptyList();
        }

        Aggregate agg = response.aggregations().get("banner");

        Aggregate itemsAgg = null;

        if (agg.isTopHits()) {
            itemsAgg = agg;
        } else if (agg.isFilter()) {
            FilterAggregate filterAgg = agg.filter();
            itemsAgg = filterAgg.aggregations().get("items");
        }

        if (itemsAgg == null || !itemsAgg.isTopHits()) return Collections.emptyList();

        List<R> banner = new ArrayList<>();

        for (Hit<JsonData> hit : itemsAgg.topHits().hits().hits()) {

            if (hit.source() == null) continue;

            D doc = converter.convert(hit.source(), documentClass);
            if (doc == null) continue;

            banner.add(mapper.map(doc, lang));
        }

        return banner;
    }

    // ---------------- SECTIONS ----------------
    private <D extends SectionDocument, R> List<SectionResponse<R>> mapSections(
            SearchResponse<D> response,
            Class<D> documentClass,
            SectionContentMapper<D, R> mapper,
            ContentType sectionType,
            String lang
    ) {

        if (response.aggregations() == null || !response.aggregations().containsKey("sections")) {
            return Collections.emptyList();
        }

        LongTermsAggregate sectionsAgg =
                response.aggregations().get("sections").lterms();

        List<SectionResponse<R>> sections = new ArrayList<>();

        for (LongTermsBucket bucket : sectionsAgg.buckets().array()) {


            SectionDocument sectionDocument = extractSectionDetail(bucket, documentClass);
            if (sectionDocument == null) {
                continue;
            }


            if (sectionDocument.getSectionTitleEn() != null &&
                    sectionDocument.getSectionTitleEn().equalsIgnoreCase("banner")) {
                continue;
            }
            SectionResponse<R> section = new SectionResponse<>();
            section.setSectionId(bucket.key().signed());
            section.setSectionType(sectionType);

            section.setSectionName(LocalizedText.getName(sectionDocument.getSectionTitleEn(),
                    sectionDocument.getSectionTitleAr(),lang));

            List<R> content = new ArrayList<>();

            Aggregate itemsAgg = bucket.aggregations().get("items");

            if (itemsAgg != null && itemsAgg.isTopHits()) {

                for (Hit<JsonData> hit : itemsAgg.topHits().hits().hits()) {

                    if (hit.source() == null) continue;

                    D doc = converter.convert(hit.source(), documentClass);
                    if (doc == null) continue;

                    content.add(mapper.map(doc, lang));
                }
            }

            section.setContent(content);
            sections.add(section);
        }

        return sections;
    }

    // ---------------- SECTION NAME ----------------
    private <D extends SectionDocument> SectionDocument extractSectionDetail(
            LongTermsBucket bucket,
            Class<D> documentClass
    ) {

        Aggregate nameAgg = bucket.aggregations().get("section_name");

        if (nameAgg == null || !nameAgg.isTopHits()) {
            return null;
        }

        List<Hit<JsonData>> hits = nameAgg.topHits().hits().hits();

        if (hits.isEmpty() || hits.getFirst().source() == null) {
            return null;
        }

        return converter.convert(hits.getFirst().source(), documentClass);
    }

// --------------------------MSearch

    public <D, R> List<R> mapSearchItems(
            MsearchResponse<JsonNode> result,
            int responseIndex,
            Class<D> documentClass,
            SectionContentMapper<D, R> mapper,
            String lang
    ) {
        return mapSources(result, responseIndex, documentClass).stream()
                .map(document -> mapper.map(document, lang))
                .toList();
    }

    public <D, R> List<R> mapSearchItems(
            MsearchResponse<JsonNode> result,
            int responseIndex,
            Class<D> documentClass,
            SectionContentMapper<D, R> mapper,
            BiConsumer<D, JsonNode> normalizer,
            String lang
    ) {
        return mapSources(result, responseIndex, documentClass, normalizer).stream()
                .map(document -> mapper.map(document, lang))
                .toList();
    }

    public <D> List<D> mapSources(MsearchResponse<JsonNode> result, int responseIndex, Class<D> documentClass) {
        return mapSources(result, responseIndex, documentClass, (document, source) -> {
        });
    }

    public <D> List<D> mapSources(
            MsearchResponse<JsonNode> result,
            int responseIndex,
            Class<D> documentClass,
            BiConsumer<D, JsonNode> normalizer
    ) {
        if (result == null || result.responses() == null || result.responses().size() <= responseIndex) {
            return Collections.emptyList();
        }

        if (!result.responses().get(responseIndex).isResult()) {
            return Collections.emptyList();
        }

        return result.responses().get(responseIndex).result().hits().hits().stream()
                .map(hit -> {
                    D document = converter.convertSource(hit.source(), documentClass);
                    if (document != null) {
                        normalizer.accept(document, hit.source());
                    }
                    return document;
                })
                .filter(Objects::nonNull)
                .toList();
    }




}
