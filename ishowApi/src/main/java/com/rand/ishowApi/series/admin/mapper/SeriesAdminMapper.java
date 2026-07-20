package com.rand.ishowApi.series.admin.mapper;

import com.rand.ishowApi.series.admin.api.response.SeriesAdminResponse;
import com.rand.ishowApi.series.admin.api.response.SeriesSectionAdminResponse;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.openSearch.model.SeriesDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SeriesAdminMapper {

    public List<SeriesAdminResponse> toFilterResponse(List<Series> seriesList) {
        return seriesList.stream().map(this::toFilterResponseItem).toList();
    }

    private SeriesAdminResponse toFilterResponseItem(Series series) {
        SeriesAdminResponse response = new SeriesAdminResponse();
        response.setSeriesId(series.getId());
        response.setTitleEn(series.getTitleEn());
        response.setTitleAr(series.getTitleAr());
        response.setDescriptionEn(series.getDescriptionEn());
        response.setDescriptionAr(series.getDescriptionAr());
        response.setPoster(series.getPoster());
        response.setBadge(series.getBadge());
        response.setTags(series.getTags());
        response.setLanguage(series.getLanguage());
        response.setHasRight(series.getHasRight());
        response.setIsKids(series.getIsKids());
        response.setActive(series.getActive());
        response.setIsSports(series.getIsSports());
        response.setRate(series.getRating());
        response.setReleaseYear(series.getReleaseYear());
        response.setSubtitleLanguages(series.getSubtitleLanguages());
        response.setAudioLanguages(series.getAudioLanguages());
        response.setIsPublish(series.getIsPublish());
        return response;
    }

    public SeriesSectionAdminResponse toSeriesSectionResponse(SeriesDocument document) {
        if (document == null) {
            return null;
        }
        SeriesSectionAdminResponse response = new SeriesSectionAdminResponse();
        response.setSeriesId(document.getSeriesId());
        response.setTitleEn(document.getTitleEn());
        response.setTitleAr(document.getTitleAr());
        response.setDescriptionEn(document.getDescriptionEn());
        response.setDescriptionAr(document.getDescriptionAr());
        response.setPoster(document.getPoster());
        response.setBadge(document.getBadge());
        response.setTags(document.getTags());
        response.setActors(document.getActors());
        response.setReleaseYear(document.getReleaseYear());
        response.setIsPublish(document.getIsPublish());
        response.setHasRight(document.getHasRight());
        response.setLanguage(document.getLanguage());
        response.setActive(document.getActive());
        response.setIsKids(document.getIsKids());
        response.setIsSports(document.getIsSports());
        response.setRating(document.getRating());
        response.setSubtitleLanguages(document.getSubtitleLanguages());
        response.setAudioLanguages(document.getAudioLanguages());
        response.setIsTop(document.getIsTop());
        response.setCreateDate(document.getCreateDate());
        response.setSeasonCount(document.getSeasonCount());

        response.setSectionId(document.getSectionId());
        response.setSectionTitleAr(document.getSectionTitleAr());
        response.setSectionTitleEn(document.getSectionTitleEn());
        response.setSectionOrder(document.getSectionOrder());
        response.setSectionActive(document.isSectionActive());
        response.setSectionPublish(document.isSectionPublish());

        return response;
    }

    public List<SeriesSectionAdminResponse> toSeriesSectionResponseList(List<SeriesDocument> documents) {
        return documents.stream()
                .map(this::toSeriesSectionResponse)
                .collect(Collectors.toList());
    }
}

