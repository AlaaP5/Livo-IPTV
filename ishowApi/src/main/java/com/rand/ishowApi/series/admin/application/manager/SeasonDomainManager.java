package com.rand.ishowApi.series.admin.application.manager;


import com.rand.ishowApi.metadata.ActorMeta;
import com.rand.ishowApi.metadata.Trailer;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.series.admin.api.request.AddSeriesSeasonAdminRequest;
import com.rand.ishowApi.series.admin.api.request.UpdateSeriesSeasonAdminRequest;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.entity.SeriesSeason;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeasonDomainManager {
    public SeriesSeason createSeason(AddSeriesSeasonAdminRequest request, OriginalUploadMetadata poster, List<ActorMeta> actorMetaList) {
        SeriesSeason season = new SeriesSeason();
        season.setSeriesId(request.getSeriesId());
        season.setSeasonNumber(request.getSeasonNumber());
        season.setTitleEn(request.getTitleEn());
        season.setTitleAr(request.getTitleAr());
        season.setDescriptionEn(request.getDescriptionEn());
        season.setDescriptionAr(request.getDescriptionAr());
        season.setReleaseYear(request.getReleaseYear());
        season.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        season.setPoster(poster);
        season.setActors(actorMetaList);
        season.setRating(request.getRating());
        return season;
    }

    public void updateSeason(
            SeriesSeason season,
            UpdateSeriesSeasonAdminRequest request,
            OriginalUploadMetadata poster
    ) {
        if (request.getSeasonNumber() != null) {
            season.setSeasonNumber(request.getSeasonNumber());
        }
        if (request.getTitleEn() != null) {
            season.setTitleEn(request.getTitleEn());
        }
        if (request.getTitleAr() != null) {
            season.setTitleAr(request.getTitleAr());
        }
        if (request.getDescriptionEn() != null) {
            season.setDescriptionEn(request.getDescriptionEn());
        }
        if (request.getDescriptionAr() != null) {
            season.setDescriptionAr(request.getDescriptionAr());
        }
        if (request.getReleaseYear() != null) {
            season.setReleaseYear(request.getReleaseYear());
        }
        if (request.getActive() != null) {
            season.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        }
        if (request.getRating() != null) {
            season.setRating(request.getRating());
        }

        if (poster != null) {
            season.setPoster(poster);
        }
    }


    public void addSeasonFiles(SeriesSeason season,  OriginalUploadMetadata trailer ){

        Trailer t = season.getTrailer() != null ? season.getTrailer() : new Trailer();
        t.setOriginalUploadMetadata(trailer);
        t.setIsPublish(false);
        t.setTranscodeStatus(TranscodeStatus.PENDING);
        season.setTrailer(t);
    }


    public void updateSeriesSeasonCount(Series series, Integer seasonCount ){
        series.setSeasonCount(seasonCount);
    }
}
