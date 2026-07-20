package com.rand.ishowApi.series.admin.application.service;


import com.rand.ishowApi.actor.domain.entity.Actor;
import com.rand.ishowApi.actor.domain.repository.ActorRepository;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.ActorMeta;
import com.rand.ishowApi.metadata.mapper.MetaHelper;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.series.admin.api.request.AddSeriesSeasonAdminRequest;
import com.rand.ishowApi.series.admin.api.request.UpdateSeriesSeasonAdminRequest;
import com.rand.ishowApi.series.admin.application.manager.SeasonDomainManager;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.entity.SeriesSeason;
import com.rand.ishowApi.series.admin.domain.repository.SeriesRepository;
import com.rand.ishowApi.series.admin.domain.repository.SeriesSeasonRepository;
import com.rand.ishowApi.upload.service.UploadServiceAsync;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SeriesSeasonAdminService {
    private final SeriesSeasonRepository repository;
    private final SeriesRepository seriesRepository;
    private final SeasonDomainManager manager;
    private final UploadServiceAsync uploadServiceAsync;
    private final ActorRepository actorRepository;
    private final MetaHelper metaHelper;


    // ================================================
    // =========== Season
    // =================================================

    public SeriesSeason addSeriesSeason(AddSeriesSeasonAdminRequest request) {

        Series series = seriesRepository.findById(request.getSeriesId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_NOT_FOUND, null));

        List<ActorMeta> actorMetaList = null;
        if (request.getActors() != null) {
            List<Actor> actors = actorRepository.findAllById(request.getActors());
            actorMetaList = metaHelper.mapMetaActor(actors);
        }

        OriginalUploadMetadata posterMetadata = null;
        if (request.getPoster() != null) {
            CompletableFuture<OriginalUploadMetadata> posterFuture = uploadServiceAsync.uploadSeasonPosterAsync(
                    request.getPoster(),
                    series.getId(),
                    request.getSeasonNumber()
            );
            CompletableFuture.allOf(posterFuture).join();
            posterMetadata = posterFuture.join();
        }

        SeriesSeason season = manager.createSeason(request, posterMetadata, actorMetaList);
        repository.save(season);

        if (request.getTrailer() != null) {
            CompletableFuture<OriginalUploadMetadata> trailerFuture = uploadServiceAsync.uploadSeasonTrailerAsync(
                    request.getTrailer(),
                    series.getId(),
                    season.getSeasonNumber()
            );
            CompletableFuture.allOf(trailerFuture).join();
            OriginalUploadMetadata trailerMetadata = trailerFuture.join();
            manager.addSeasonFiles(season, trailerMetadata);
        }

        updateSeriesCount(series);
        return season;

    }

    public SeriesSeason updateSeriesSeason(UpdateSeriesSeasonAdminRequest request) {
        Series series = seriesRepository.findById(request.getSeriesId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_NOT_FOUND, null));
        SeriesSeason season =repository.findById(request.getSeasonId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_SEASON_NOT_FOUND,null ));


        Integer targetSeasonNumber = request.getSeasonNumber() != null
                ? request.getSeasonNumber()
                : season.getSeasonNumber();

        OriginalUploadMetadata posterMetadata = null;
        if (request.getPoster() != null) {
            CompletableFuture<OriginalUploadMetadata> posterFuture = uploadServiceAsync.uploadSeasonPosterAsync(
                    request.getPoster(),
                    series.getId(),
                    targetSeasonNumber
            );
            CompletableFuture.allOf(posterFuture).join();
            posterMetadata = posterFuture.join();

            if (season.getPoster() != null) {
                uploadServiceAsync.removeOldFileAsync(season.getPoster());
            }
        }

        manager.updateSeason(season, request, posterMetadata);

        if (request.getTrailer() != null) {
            CompletableFuture<OriginalUploadMetadata> trailerFuture = uploadServiceAsync.uploadSeasonTrailerAsync(
                    request.getTrailer(),
                    series.getId(),
                    season.getSeasonNumber()
            );
            CompletableFuture.allOf(trailerFuture).join();
            OriginalUploadMetadata trailerMetadata = trailerFuture.join();

            if (season.getTrailer() != null && season.getTrailer().getOriginalUploadMetadata() != null) {
                uploadServiceAsync.removeOldFileAsync(season.getTrailer().getOriginalUploadMetadata());
            }

            if (trailerMetadata != null) {
                manager.addSeasonFiles(season, trailerMetadata);
            }
        }


        repository.save(season);
        updateSeriesCount(series);
        return season;
    }

    public List<SeriesSeason> findSeriesSeasonBySeriesId(String seriesId) {
        List<SeriesSeason> seasons = repository.findBySeriesId(seriesId);
        if (seasons == null) {
            return List.of();
        }

        return seasons;
    }

   private void updateSeriesCount(Series series){
       Integer seasonCount=repository.countBySeriesIdAndActiveTrue(series.getId());
       manager.updateSeriesSeasonCount(series, seasonCount);
       seriesRepository.save(series);
   }



}
