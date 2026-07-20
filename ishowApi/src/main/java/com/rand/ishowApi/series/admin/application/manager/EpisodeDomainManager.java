package com.rand.ishowApi.series.admin.application.manager;


import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.series.admin.api.request.AddSeriesEpisodeAdminRequest;
import com.rand.ishowApi.series.admin.api.request.UpdateSeriesEpisodeAdminRequest;
import com.rand.ishowApi.series.admin.domain.entity.SeriesEpisode;
import com.rand.ishowApi.series.admin.domain.entity.SeriesSeason;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EpisodeDomainManager {


    public SeriesEpisode createEpisode(
            AddSeriesEpisodeAdminRequest request,
            OriginalUploadMetadata poster,
            OriginalUploadMetadata originalFile,
            List<OriginalUploadMetadata> subtitles
    ) {
        SeriesEpisode episode = new SeriesEpisode();
        episode.setSeriesId(request.getSeriesId());
        episode.setSeasonId(request.getSeasonId());
        episode.setEpisodeNumber(request.getEpisodeNumber());
        episode.setTitleEn(request.getTitleEn());
        episode.setTitleAr(request.getTitleAr());
        episode.setPoster(poster);
        episode.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        episode.setIsPublish(false);
        episode.setTranscodeStatus(TranscodeStatus.PENDING);
        episode.setRate(request.getRate());
        episode.setDuration(request.getDuration());
        episode.setAccessType(request.getAccessType());
        episode.setBadge(request.getBadge());
        episode.setOriginalUploadMetadata(originalFile);
        episode.setSubtitles(subtitles);
        return episode;
    }

    public void updateEpisode(
            SeriesEpisode episode,
            UpdateSeriesEpisodeAdminRequest request,
            OriginalUploadMetadata poster,
            OriginalUploadMetadata originalFile,
            List<OriginalUploadMetadata> subtitles
    ) {
        if (request.getEpisodeNumber() != null) {
            episode.setEpisodeNumber(request.getEpisodeNumber());
        }
        if (request.getTitleEn() != null) {
            episode.setTitleEn(request.getTitleEn());
        }
        if (request.getTitleAr() != null) {
            episode.setTitleAr(request.getTitleAr());
        }
        if (request.getActive() != null) {
            episode.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        }

        if (request.getRate() != null) {
            episode.setRate(request.getRate());
        }
        if (request.getDuration() != null) {
            episode.setDuration(request.getDuration());
        }
        if (request.getAccessType() != null) {
            episode.setAccessType(request.getAccessType());
        }
        if (request.getBadge() != null) {
            episode.setBadge(request.getBadge());
        }
        if (poster != null) {
            episode.setPoster(poster);
        }
        if (originalFile != null) {
            episode.setOriginalUploadMetadata(originalFile);
        }
        if (subtitles != null) {
            episode.setSubtitles(subtitles);
        }
    }


    public void updateSeriesEpisodeCount(SeriesSeason season, Integer count ){
        season.setEpisodeCount(count);
    }

    public void addEpisodeTranscodeFile(SeriesEpisode episode, TranscodeMetaData transcodeMetaData) {
        episode.setTranscodeMetaData(transcodeMetaData);
        episode.setTranscodeStatus(TranscodeStatus.COMPLETED);
    }

    public void publishEpisode(SeriesEpisode episode) {
        episode.setIsPublish(true);
        // No publishDate field on SeriesEpisode entity; only set publish flag
    }
}
