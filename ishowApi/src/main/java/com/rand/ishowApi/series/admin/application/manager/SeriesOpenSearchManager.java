package com.rand.ishowApi.series.admin.application.manager;


import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.openSearch.model.SeriesDocument;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class SeriesOpenSearchManager {

    public void updateSeriesSectionDocument(SeriesDocument doc, Series series) {
        doc.setTitleEn(series.getTitleEn());
        doc.setTitleAr(series.getTitleAr());
        doc.setDescriptionEn(series.getDescriptionEn());
        doc.setDescriptionAr(series.getDescriptionAr());
        doc.setPoster(series.getPoster());
        doc.setTags(series.getTags());
        doc.setActors(series.getActors());
        doc.setBadge(series.getBadge());
        doc.setReleaseYear(series.getReleaseYear());
        doc.setIsPublish(series.getIsPublish());
        doc.setHasRight(series.getHasRight());
        doc.setLanguage(series.getLanguage());
        doc.setActive(series.getActive());
        doc.setIsKids(series.getIsKids());
        doc.setIsSports(series.getIsSports());
        doc.setRating(series.getRating());
        doc.setSubtitleLanguages(series.getSubtitleLanguages());
        doc.setAudioLanguages(series.getAudioLanguages());
        doc.setSeasonCount(series.getSeasonCount());
    }

    public void setIsTop(SeriesDocument doc, String isTop){
        doc.setIsTop(BooleanConverter.getActiveBoolean(isTop));
    }

    public SeriesDocument createSeriesSectionDocument(Series series, Section section, String isTop) {
        SeriesDocument doc = new SeriesDocument();
        doc.setSectionId(section.getId());
        doc.setSectionTitleAr(section.getTitleAr());
        doc.setSectionTitleEn(section.getTitleEn());
        doc.setSectionOrder(section.getOrder());
        doc.setSectionActive(section.getActive());
        doc.setSectionPublish(section.getPublish());
        doc.setSeriesId(series.getId());
        doc.setTitleEn(series.getTitleEn());
        doc.setTitleAr(series.getTitleAr());
        doc.setDescriptionEn(series.getDescriptionEn());
        doc.setDescriptionAr(series.getDescriptionAr());
        doc.setPoster(series.getPoster());
        doc.setBadge(series.getBadge());
        doc.setTags(series.getTags());
        doc.setActors(series.getActors());
        doc.setReleaseYear(series.getReleaseYear());
        doc.setIsPublish(series.getIsPublish());
        doc.setHasRight(series.getHasRight());
        doc.setLanguage(series.getLanguage());
        doc.setActive(series.getActive());
        doc.setIsKids(series.getIsKids());
        doc.setIsSports(series.getIsSports());
        doc.setRating(series.getRating());
        doc.setSubtitleLanguages(series.getSubtitleLanguages());
        doc.setAudioLanguages(series.getAudioLanguages());
        doc.setSeasonCount(series.getSeasonCount());
        doc.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        doc.setIsTop(BooleanConverter.getActiveBoolean(isTop));
        return doc;
    }
}
