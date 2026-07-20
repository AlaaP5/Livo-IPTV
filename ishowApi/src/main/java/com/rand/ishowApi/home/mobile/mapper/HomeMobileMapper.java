package com.rand.ishowApi.home.mobile.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.rand.ishowApi.channel.admin.domain.openSearch.model.ChannelDocument;
import com.rand.ishowApi.channel.mobile.mapper.ChannelDocMapper;
import com.rand.ishowApi.clip.admin.domain.openSearch.model.ClipDocument;
import com.rand.ishowApi.clip.mobile.mapper.ClipDocMapper;
import com.rand.ishowApi.home.admin.domain.model.HomeBanner;
import com.rand.ishowApi.home.mobile.api.response.BannerResponse;
import com.rand.ishowApi.home.mobile.api.response.HomeMobileResponse;
import com.rand.ishowApi.home.mobile.api.response.HomeSectionResponse;
import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.movie.admin.domain.openSearch.model.MovieDocument;
import com.rand.ishowApi.movie.mobile.mapper.MovieDocMapper;
import com.rand.ishowApi.openSearch.mapper.SectionAggregationMapper;
import com.rand.ishowApi.openSearch.mapper.SectionContentMapper;
import com.rand.ishowApi.openSearch.model.SectionDocument;
import com.rand.ishowApi.series.admin.domain.openSearch.model.SeriesDocument;
import com.rand.ishowApi.series.mobile.mapper.SeriesDocMapper;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.model.TvProgramDocument;
import com.rand.ishowApi.tvProgram.mobile.mapper.TvProgramDocMapper;
import com.rand.ishowApi.utils.LocalizedText;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.core.MsearchResponse;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class HomeMobileMapper {
    private final SectionAggregationMapper sectionAggregationMapper;
    private final MovieDocMapper movieDocMapper;
    private final SeriesDocMapper seriesDocMapper;
    private final TvProgramDocMapper tvProgramDocMapper;
    private final ClipDocMapper clipDocMapper;
    private final ChannelDocMapper channelDocMapper;

    public HomeMobileResponse maptoHomeMobileResponse(MsearchResponse<JsonNode> result, String lang) {
        HomeMobileResponse homeResponse = new HomeMobileResponse();

        homeResponse.setBanner(mapHomeBanner(
                sectionAggregationMapper.mapSources(result, 0, HomeBanner.class),
                lang
        ));

        List<HomeSectionResponse> sections = new ArrayList<>();
        sections.addAll(mapHomeSectionsByContentType(result, 1, MovieDocument.class, movieDocMapper, ContentType.MOVIES, lang));
        sections.addAll(mapHomeSectionsByContentType(result, 2, SeriesDocument.class, seriesDocMapper, ContentType.SERIES, lang));
        sections.addAll(mapHomeSectionsByContentType(result, 3, TvProgramDocument.class, tvProgramDocMapper, ContentType.TV_PROGRAMS, lang));
        sections.addAll(mapHomeSectionsByContentType(result, 4, ClipDocument.class, clipDocMapper, ContentType.CLIPS, lang));
        sections.addAll(mapHomeSectionsByContentType(result, 5, ChannelDocument.class, channelDocMapper, ContentType.CHANNELS, lang));

        sections.sort(Comparator.comparing(
                HomeSectionResponse::getSectionOrder,
                Comparator.nullsLast(Integer::compareTo)
        ));

        homeResponse.setSections(sections);
        return homeResponse;
    }

    private List<BannerResponse> mapHomeBanner(List<HomeBanner> banners, String lang) {
        if (banners == null) {
            return List.of();
        }

        return banners.stream()
                .map(banner -> mapBanner(banner, lang))
                .toList();
    }

    private BannerResponse mapBanner(HomeBanner banner, String lang) {
        BannerResponse response = new BannerResponse();
        response.setId(banner.getContentId());
        response.setType(banner.getContentType());
        response.setImage(banner.getPoster());
        response.setTitle(LocalizedText.getName(
                banner.getContentEnglishTitle(),
                banner.getContentArabicTitle(),
                lang
        ));

        if (banner.getTagMeta() == null) {
            response.setTags(List.of());
        } else {
            response.setTags(banner.getTagMeta().stream().map(tag -> {
                LookupResponse tagResponse = new LookupResponse();
                tagResponse.setId(tag.getId());
                tagResponse.setName(LocalizedText.getName(tag.getTitleEn(), tag.getTitleAr(), lang));
                return tagResponse;
            }).toList());
        }

        return response;
    }

    private <D extends SectionDocument, R> List<HomeSectionResponse> mapHomeSectionsByContentType(
            MsearchResponse<JsonNode> result,
            int responseIndex,
            Class<D> documentClass,
            SectionContentMapper<D, R> mapper,
            ContentType type,
            String lang
    ) {
        List<D> documents = sectionAggregationMapper.mapSources(result, responseIndex, documentClass);
        Map<Long, List<D>> bySectionId = new LinkedHashMap<>();

        for (D document : documents) {
            if (document.getSectionId() == null) {
                continue;
            }
            bySectionId.computeIfAbsent(document.getSectionId(), ignored -> new ArrayList<>()).add(document);
        }

        return bySectionId.values().stream()
                .map(sectionDocuments -> mapHomeSectionGroup(sectionDocuments, mapper, type, lang))
                .sorted(Comparator.comparing(
                        HomeSectionResponse::getSectionOrder,
                        Comparator.nullsLast(Integer::compareTo)
                ))
                .toList();
    }

    private <D extends SectionDocument, R> HomeSectionResponse mapHomeSectionGroup(
            List<D> sectionDocuments,
            SectionContentMapper<D, R> mapper,
            ContentType type,
            String lang
    ) {
        D first = sectionDocuments.getFirst();

        HomeSectionResponse response = new HomeSectionResponse();
        response.setSectionId(first.getSectionId());
        response.setSectionTitle(LocalizedText.getName(first.getSectionTitleEn(), first.getSectionTitleAr(), lang));
        response.setSectionType(type);
        response.setSectionOrder(first.getSectionOrder());
        response.setContents(sectionDocuments.stream()
                .map(document -> mapper.map(document, lang))
                .toList());

        return response;
    }
}
