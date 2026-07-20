package com.rand.ishowApi.series.admin.mapper;

import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.openSearch.model.SeriesSearchDocument;
import org.springframework.stereotype.Component;

@Component
public class SeriesSearchMapper {

    public SeriesSearchDocument toDoc(Series series) {
        if (series == null) return null;

        SeriesSearchDocument doc = new SeriesSearchDocument();

        doc.setId(series.getId());
        doc.setTitleEn(series.getTitleEn());
        doc.setTitleAr(series.getTitleAr());
        doc.setDescriptionEn(series.getDescriptionEn());
        doc.setDescriptionAr(series.getDescriptionAr());

        doc.setBadge(series.getBadge());
        doc.setPoster(series.getPoster());

        doc.setTags(series.getTags());
        doc.setActors(series.getActors());

        doc.setReleaseYear(series.getReleaseYear());
        doc.setSeasonCount(series.getSeasonCount());

        doc.setSubtitleLanguages(series.getSubtitleLanguages());
        doc.setAudioLanguages(series.getAudioLanguages());

        doc.setIsPublish(series.getIsPublish());
        doc.setHasRight(series.getHasRight());
        doc.setLanguage(series.getLanguage());

        doc.setActive(series.getActive());
        doc.setIsKids(series.getIsKids());
        doc.setIsSports(series.getIsSports());

        doc.setRating(series.getRating());

        return doc;
    }
}

