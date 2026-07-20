package com.rand.ishowApi.series.admin.application.service;


import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.series.admin.api.request.AddSeriesEpisodeAdminRequest;
import com.rand.ishowApi.series.admin.api.request.UpdateSeriesEpisodeAdminRequest;
import com.rand.ishowApi.series.admin.application.manager.EpisodeDomainManager;
import com.rand.ishowApi.series.admin.application.service.SeriesOpenSearchService;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.entity.SeriesEpisode;
import com.rand.ishowApi.series.admin.domain.entity.SeriesSeason;
import com.rand.ishowApi.series.admin.domain.repository.SeriesEpisodeRepository;
import com.rand.ishowApi.series.admin.domain.repository.SeriesRepository;
import com.rand.ishowApi.series.admin.domain.repository.SeriesSeasonRepository;
import com.rand.ishowApi.upload.service.UploadServiceAsync;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.io.IOException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SeriesEpisodeAdminService {
    private final SeriesEpisodeRepository repository;
    private final  SeriesRepository seriesRepository;
    private final  SeriesSeasonRepository seasonRepository;
    private final  UploadServiceAsync uploadServiceAsync;
    private final  EpisodeDomainManager manager;

 public SeriesEpisode addSeriesEpisode(AddSeriesEpisodeAdminRequest request) {
     Series series = seriesRepository.findById(request.getSeriesId())
             .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_NOT_FOUND, null));
     SeriesSeason season =seasonRepository.findById(request.getSeasonId())
             .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_SEASON_NOT_FOUND,null ));

    OriginalUploadMetadata posterMetadata = null;
    if (request.getPoster() != null) {
        CompletableFuture<OriginalUploadMetadata> posterFuture = uploadServiceAsync.uploadEpisodePosterAsync(
                request.getPoster(),
                series.getId(),
                season.getSeasonNumber(),
                request.getEpisodeNumber()
        );
        CompletableFuture.allOf(posterFuture).join();
        posterMetadata = posterFuture.join();
    }



    OriginalUploadMetadata originalVideoMetadata = null;
    if (request.getFile() != null) {
        CompletableFuture<OriginalUploadMetadata> originalFuture = uploadServiceAsync.uploadEpisodeOriginalVideoAsync(
                request.getFile(),
                series.getId(),
                season.getSeasonNumber(),
                request.getEpisodeNumber()
        );
        CompletableFuture.allOf(originalFuture).join();
        originalVideoMetadata = originalFuture.join();
    }

    List<OriginalUploadMetadata> subtitles = null;
    if (request.getSubtitles() != null) {
        CompletableFuture<List<OriginalUploadMetadata>> subtitlesFuture = uploadServiceAsync.uploadEpisodeSubtitleAsync(
                request.getSubtitles(),
                series.getId(),
                season.getSeasonNumber(),
                request.getEpisodeNumber()
        );
        CompletableFuture.allOf(subtitlesFuture).join();
        subtitles = subtitlesFuture.join();
    }

    SeriesEpisode episode = manager.createEpisode(request, posterMetadata, originalVideoMetadata, subtitles);

     repository.save(episode);
     updateSeasonEpisodeCount(season);
    return episode;
}

    public SeriesEpisode updateSeriesEpisode(UpdateSeriesEpisodeAdminRequest request) {
        Series series = seriesRepository.findById(request.getSeriesId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_NOT_FOUND, null));
        SeriesSeason season =seasonRepository.findById(request.getSeasonId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_SEASON_NOT_FOUND,null ));

        SeriesEpisode episode = repository.findById(request.getEpisodeId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_EPISODE_NOT_FOUND,null ));

        Integer targetEpisodeNumber = request.getEpisodeNumber() != null
                ? request.getEpisodeNumber()
                : episode.getEpisodeNumber();

        OriginalUploadMetadata posterMetadata = null;
        if (request.getPoster() != null) {
            CompletableFuture<OriginalUploadMetadata> posterFuture = uploadServiceAsync.uploadEpisodePosterAsync(
                    request.getPoster(),
                    series.getId(),
                    season.getSeasonNumber(),
                    targetEpisodeNumber
            );
            CompletableFuture.allOf(posterFuture).join();
            posterMetadata = posterFuture.join();

            if (episode.getPoster() != null) {
                uploadServiceAsync.removeOldFileAsync(episode.getPoster());
            }
        }

        OriginalUploadMetadata originalVideoMetadata = null;
        if (request.getFile() != null) {
            CompletableFuture<OriginalUploadMetadata> originalFuture = uploadServiceAsync.uploadEpisodeOriginalVideoAsync(
                    request.getFile(),
                    series.getId(),
                    season.getSeasonNumber(),
                    targetEpisodeNumber
            );
            CompletableFuture.allOf(originalFuture).join();
            originalVideoMetadata = originalFuture.join();

            if (episode.getOriginalUploadMetadata() != null) {
                uploadServiceAsync.removeOldFileAsync(episode.getOriginalUploadMetadata());
            }
        }

        List<OriginalUploadMetadata> subtitles = null;
        if (request.getSubtitles() != null) {
            CompletableFuture<List<OriginalUploadMetadata>> subtitlesFuture = uploadServiceAsync.uploadEpisodeSubtitleAsync(
                    request.getSubtitles(),
                    series.getId(),
                    season.getSeasonNumber(),
                    targetEpisodeNumber
            );
            CompletableFuture.allOf(subtitlesFuture).join();
            subtitles = subtitlesFuture.join();

            if (episode.getSubtitles() != null) {
                for (OriginalUploadMetadata subtitle : episode.getSubtitles()) {
                    uploadServiceAsync.removeOldFileAsync(subtitle);
                }
            }
        }

        manager.updateEpisode(episode, request, posterMetadata, originalVideoMetadata, subtitles);

        repository.save(episode);
        updateSeasonEpisodeCount(season);

        return episode;
    }

    public Page<SeriesEpisode> findSeriesEpisodeBySeasonId(String seriesId, String seasonId, int page, int size) {
        int pageIndex = Math.max(page, 1) - 1;
        int pageSize = Math.max(size, 1);
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.ASC, "episodeNumber"));
        return repository.findPageBySeriesIdAndSeasonId(seriesId, seasonId, pageable);
    }

    private void updateSeasonEpisodeCount(SeriesSeason season){
         Integer count =repository.countBySeasonIdAndActiveTrueAndIsPublishTrue(season.getId());
         manager.updateSeriesEpisodeCount(season, count);
         seasonRepository.save(season);

    }

    public void updateSeriesEpisodeTranscodeResult(String episodeId, TranscodeMetaData metaData) throws IOException {
        SeriesEpisode episode = repository.findById(episodeId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_EPISODE_NOT_FOUND, null));

        manager.addEpisodeTranscodeFile(episode, metaData);
        repository.save(episode);

    }

    public SeriesEpisode publishEpisode(String episodeId) throws IOException {
        SeriesEpisode episode = repository.findById(episodeId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_EPISODE_NOT_FOUND, null));

        if (episode.getTranscodeStatus() != null && episode.getTranscodeStatus() != com.rand.ishowApi.metadata.TranscodeStatus.COMPLETED) {
            throw new CustomException(ApiResponseCode.SERIES_EPISODE_TRANSCODE_NOT_COMPLETE, null);
        }

         manager.publishEpisode(episode);
        repository.save(episode);

        SeriesSeason season = seasonRepository.findById(episode.getSeasonId())
                .orElseThrow(() -> new CustomException(ApiResponseCode.SERIES_SEASON_NOT_FOUND, null));

        updateSeasonEpisodeCount(season);

        return episode;
    }

}
