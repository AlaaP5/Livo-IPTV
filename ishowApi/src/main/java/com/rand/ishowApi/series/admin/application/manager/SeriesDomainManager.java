package com.rand.ishowApi.series.admin.application.manager;

import com.rand.ishowApi.metadata.ActorMeta;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.series.admin.api.request.AddSeriesAdminRequest;
import com.rand.ishowApi.series.admin.api.request.UpdateSeriesAdminRequest;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeriesDomainManager {

    // ================================================
    // =========== Series
    // =================================================

    public Series create(AddSeriesAdminRequest req, List<TagMeta> tags, List<ActorMeta> actorMetaList) {
        return Series.builder()
                .titleEn(req.getTitleEn())
                .titleAr(req.getTitleAr())
                .descriptionEn(req.getDescriptionEn())
                .descriptionAr(req.getDescriptionAr())
                .releaseYear(req.getReleaseYear())
                .badge(req.getBadge())
                .tags(tags)
                .actors(actorMetaList)
                .hasRight(BooleanConverter.getActiveBoolean(req.getHasRight()))
                .language(req.getLanguage())
                .isKids(BooleanConverter.getActiveBoolean(req.getIsKids()))
                .isSports(BooleanConverter.getActiveBoolean(req.getIsSports()))
                .rating(req.getRate())
                .subtitleLanguages(req.getSubtitleLanguages())
                .audioLanguages(req.getAudioLanguages())
                .isPublish(false)
                .active(BooleanConverter.getActiveBoolean(req.getActive()))
                .build();
    }

    public void addSeriesPoster(Series series, OriginalUploadMetadata poster) {
        series.setPoster(poster);
    }

    public void update(
            Series series,
            UpdateSeriesAdminRequest request,
            List<TagMeta> tags,
            List<ActorMeta> actors ,
            OriginalUploadMetadata poster
    ) {
        if (request.getTitleEn() != null) {
            series.setTitleEn(request.getTitleEn());
        }
        if (request.getTitleAr() != null) {
            series.setTitleAr(request.getTitleAr());
        }
        if (request.getDescriptionEn() != null) {
            series.setDescriptionEn(request.getDescriptionEn());
        }
        if (request.getDescriptionAr() != null) {
            series.setDescriptionAr(request.getDescriptionAr());
        }
        if (request.getBadge() != null) {
            series.setBadge(request.getBadge());
        }
        if (request.getLanguage() != null) {
            series.setLanguage(request.getLanguage());
        }
        if (request.getHasRight() != null) {
            series.setHasRight(BooleanConverter.getActiveBoolean(request.getHasRight()));
        }
        if (request.getIsKids() != null) {
            series.setIsKids(BooleanConverter.getActiveBoolean(request.getIsKids()));
        }
        if (request.getIsSports() != null) {
            series.setIsSports(BooleanConverter.getActiveBoolean(request.getIsSports()));
        }
        if (request.getRate() != null) {
            series.setRating(request.getRate());
        }
        if (request.getSubtitleLanguages() != null) {
            series.setSubtitleLanguages(request.getSubtitleLanguages());
        }
        if (request.getAudioLanguages() != null) {
            series.setAudioLanguages(request.getAudioLanguages());
        }
        if(request.getReleaseYear() != null){
            series.setReleaseYear(request.getReleaseYear());
        }
        if (tags != null) {
            series.setTags(tags);
        }
        if (actors != null) {
            series.setActors(actors);
        }
        if (request.getActive() != null) {
            series.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        }
        if (poster != null) {
            series.setPoster(poster);
        }
    }



}

