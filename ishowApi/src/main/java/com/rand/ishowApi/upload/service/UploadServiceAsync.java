package com.rand.ishowApi.upload.service;

import com.rand.ishowApi.channel.admin.api.request.ChannelEpgAdminRequest;
import com.rand.ishowApi.channel.admin.api.response.ChannelEpgAdminResponse;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.storage.service.StorageService;
import com.rand.ishowApi.upload.manager.UploadManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UploadServiceAsync {

    private final UploadService uploadService;
    private final StorageService storageService;
    private final UploadManager uploadManager;

    // ==================== Movie Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<List<OriginalUploadMetadata>> uploadMovieSubtitleAsync(
            List<MultipartFile> files,
            String movieId
    ) {
        List<OriginalUploadMetadata> results = files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .flatMap(file -> {
                    try {
                        return uploadService.uploadMovieSubtitle(List.of(file), movieId).stream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        return CompletableFuture.completedFuture(results);
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadMovieTranscodeAsync(
            List<MultipartFile> files,
            String movieId
    ) {
        OriginalUploadMetadata result = null;
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                try {
                    result = uploadService.uploadMovieTranscode(List.of(file), movieId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return CompletableFuture.completedFuture(result);
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadMoviePosterAsync(MultipartFile file, String movieId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadMoviePoster(file, movieId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadMovieOriginalVideoAsync(
            MultipartFile file, String movieId) {
        try {
            return CompletableFuture.completedFuture(
                    uploadService.uploadMovieOriginalVideo(file, movieId)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadMovieTrailerAsync(MultipartFile file, String movieId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadMovieTrailer(file, movieId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadMovieTranscodeTrailerAsync(MultipartFile file, String movieId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadMovieTranscodeTrailer(file, movieId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== Series Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<List<OriginalUploadMetadata>> uploadEpisodeSubtitleAsync(
            List<MultipartFile> files,
            String seriesId,
            Integer seasonNumber,
            Integer episodeId
    ) {
        List<OriginalUploadMetadata> results = files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .flatMap(file -> {
                    try {
                        return uploadService.uploadEpisodeSubtitle(List.of(file), seriesId, seasonNumber, episodeId).stream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();                ;

        return CompletableFuture.completedFuture(results);
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadEpisodeTranscodeAsync(
            MultipartFile file,
            String seriesId,
            Integer seasonNumber,
            Integer episodeId
    ) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadEpisodeTranscode(file, seriesId, seasonNumber, episodeId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadSeriesPosterAsync(MultipartFile file, String seriesId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadSeriesPoster(file, seriesId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadSeasonPosterAsync(
            MultipartFile file,
            String seriesId,
            Integer seasonNumber
    ) {
        try {
            return CompletableFuture.completedFuture(
                    uploadService.uploadSeasonPoster(file, seriesId, seasonNumber)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadSeasonTrailerAsync(MultipartFile file, String seriesId, Integer seasonNumber) {
        try {
            return CompletableFuture.completedFuture(uploadService. uploadSeasonTrailer(file, seriesId, seasonNumber));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadSeasonTranscodeTrailerAsync(MultipartFile file, String seriesId, Integer seasonNumber) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadSeasonTranscodeTrailer(file, seriesId, seasonNumber));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadEpisodePosterAsync(MultipartFile file, String seriesId, Integer seasonNumber, Integer episodeId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadEpisodePoster(file, seriesId, seasonNumber, episodeId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadEpisodeOriginalVideoAsync(MultipartFile file, String seriesId, Integer seasonNumber, Integer episodeId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadEpisodeOriginalVideo(file, seriesId, seasonNumber, episodeId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== TV Program Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<List<OriginalUploadMetadata>> uploadTvProgramEpisodeSubtitleAsync(
            List<MultipartFile> files,
            String tvProgramId,
            Integer seasonNumber,
            Integer episodeId
    ) {
        List<OriginalUploadMetadata> results = files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .flatMap(file -> {
                    try {
                        return uploadService.uploadTvProgramEpisodeSubtitle(List.of(file), tvProgramId, seasonNumber, episodeId).stream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        return CompletableFuture.completedFuture(results);
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadTvProgramEpisodeTranscodeAsync(
            MultipartFile file,
            String tvProgramId,
            Integer seasonNumber,
            Integer episodeId
    ) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadTvProgramEpisodeTranscode(file, tvProgramId, seasonNumber, episodeId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadTvProgramPosterAsync(MultipartFile file, String tvProgramId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadTvProgramPoster(file, tvProgramId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadTvProgramSeasonPosterAsync(MultipartFile file, String tvProgramId, Integer seasonNumber) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadTvProgramSeasonPoster(file, tvProgramId, seasonNumber));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadTvProgramEpisodePosterAsync(MultipartFile file, String tvProgramId, Integer seasonNumber, Integer episodeId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadTvProgramEpisodePoster(file, tvProgramId, seasonNumber, episodeId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadTvProgramEpisodeOriginalVideoAsync(MultipartFile file, String tvProgramId, Integer seasonNumber, Integer episodeId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadTvProgramEpisodeOriginalVideo(file, tvProgramId, seasonNumber, episodeId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== TV Channel Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadTvChannelLogoAsync(MultipartFile file, String channelId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadTvChannelLogo(file, channelId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== Clip Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadClipPosterAsync(MultipartFile file, String clipId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadClipPoster(file, clipId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadClipVideoAsync(MultipartFile file, String clipId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadClipVideo(file, clipId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadClipTranscodeAsync(MultipartFile file, String clipId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadClipTranscode(file, clipId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== Actors Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadActorPosterAsync(MultipartFile file, String actorId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadActorPoster(file, actorId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== Ads Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadAdPosterAsync(MultipartFile file, String adId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadAdPoster(file, adId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== Banner Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadBannerPosterAsync(MultipartFile file, String bannerId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadBannerPoster(file, bannerId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== Upcoming Match Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadUpcomingMatchPosterAsync(MultipartFile file, String upcomingMatchId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadUpcomingMatchPoster(file, upcomingMatchId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // ==================== Team Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadTeamLogoAsync(MultipartFile file, String teamId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadTeamLogo(file, teamId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== Champion Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadChampionLogoAsync(MultipartFile file, String championId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadChampionLogo(file, championId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== Profile Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<OriginalUploadMetadata> uploadProfilePosterAsync(MultipartFile file, String profileId) {
        try {
            return CompletableFuture.completedFuture(uploadService.uploadProfilePoster(file, profileId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== Remove Old File Async Methods ====================

    @Async("uploadExecutor")
    public CompletableFuture<Void> removeOldFileAsync(OriginalUploadMetadata file) {
        uploadService.removeOldFile(file);
        return CompletableFuture.completedFuture(null);
    }

    @Async("uploadExecutor")
    public CompletableFuture<Void> removeOldFileAsync(List<OriginalUploadMetadata> files) {
        files.stream()
                .filter(file -> file != null && file.getGeneratedPath() != null)
                .forEach(uploadService::removeOldFile);
        return CompletableFuture.completedFuture(null);
    }

    @Async("uploadExecutor")
    public CompletableFuture<Void> removeOldFileAsync(String filePath) {
        uploadService.removeOldFile(filePath);
        return CompletableFuture.completedFuture(null);
    }

    @Async("uploadExecutor")
    public CompletableFuture<Void> removeOldFileAsync(Set<String> filesPath) {
        filesPath.stream()
                .filter(Objects::nonNull)
                .forEach(uploadService::removeOldFile);
        return CompletableFuture.completedFuture(null);
    }

    // ==================== Extract Images From Zip ====================

    @Async("uploadExecutor")
    public CompletableFuture<List<MultipartFile>> extractImagesFromZip(MultipartFile zip) {
        try {
            return CompletableFuture.completedFuture(uploadService.extractImagesFromZip(zip));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== Parse Epg Xml ====================
    @Async("uploadExecutor")
    public CompletableFuture<List<ChannelEpgAdminRequest>> parseEpgXml(MultipartFile xmlFile, String channelId)  {

        try {
            return CompletableFuture.completedFuture( uploadService.parseEpgXml(xmlFile, channelId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}