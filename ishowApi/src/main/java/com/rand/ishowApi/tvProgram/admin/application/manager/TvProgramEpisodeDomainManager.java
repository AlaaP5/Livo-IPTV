package com.rand.ishowApi.tvProgram.admin.application.manager;

import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.tvProgram.admin.api.request.AddTvProgramEpisodeAdminRequest;
import com.rand.ishowApi.tvProgram.admin.api.request.UpdateTvProgramEpisodeAdminRequest;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramEpisode;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramSeason;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TvProgramEpisodeDomainManager {
    public TvProgramEpisode createEpisode(
            AddTvProgramEpisodeAdminRequest request,
            OriginalUploadMetadata poster,
            OriginalUploadMetadata originalFile,
            List<OriginalUploadMetadata> subtitles
    ) {
        TvProgramEpisode episode = new TvProgramEpisode();
        episode.setTvProgramId(request.getTvProgramId());
        episode.setSeasonId(request.getSeasonId());
        episode.setEpisodeNumber(request.getEpisodeNumber());
        episode.setTitleEn(request.getTitleEn());
        episode.setTitleAr(request.getTitleAr());
        episode.setPoster(poster);
        episode.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        episode.setIsPublish(false);
        episode.setTranscodeStatus(TranscodeStatus.PENDING);
        episode.setDuration(request.getDuration());
        episode.setAccessType(request.getAccessType());
        episode.setBadge(request.getBadge());
        episode.setOriginalUploadMetadata(originalFile);
        episode.setSubtitles(subtitles);
        return episode;
    }

    public void updateEpisode(
            TvProgramEpisode episode,
            UpdateTvProgramEpisodeAdminRequest request,
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

    public void updateTvProgramEpisodeCount(TvProgramSeason season, Integer episodeCount) {
        season.setEpisodeCount(episodeCount);
    }

    public void addEpisodeTranscodeFile(TvProgramEpisode episode, TranscodeMetaData transcodeMetaData) {
        episode.setTranscodeMetaData(transcodeMetaData);
        episode.setTranscodeStatus(TranscodeStatus.COMPLETED);
    }

    public void publishEpisode(TvProgramEpisode episode) {
        episode.setIsPublish(true);
    }
}

