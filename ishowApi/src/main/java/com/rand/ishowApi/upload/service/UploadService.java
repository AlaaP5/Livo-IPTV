package com.rand.ishowApi.upload.service;

import com.rand.ishowApi.channel.admin.api.request.ChannelEpgAdminRequest;
import com.rand.ishowApi.channel.admin.api.response.ChannelEpgAdminResponse;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.storage.model.contentType.StorageType;
import com.rand.ishowApi.storage.model.contentType.FileCategory;
import com.rand.ishowApi.storage.model.request.UploadRequest;
import com.rand.ishowApi.storage.service.StorageService;
import com.rand.ishowApi.upload.manager.UploadManager;
import com.rand.ishowApi.upload.model.UploadContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final UploadManager uploadManager;
    private final StorageService storageService;

    // ==================== Movie Upload Methods ====================
    public OriginalUploadMetadata uploadMoviePoster(MultipartFile file, String movieId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.MOVIE);
        context.setFileCategory(FileCategory.POSTER);
        context.setMovieId(movieId);

        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public List<OriginalUploadMetadata> uploadMovieSubtitle(List<MultipartFile> files, String movieId) throws IOException {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        List<OriginalUploadMetadata> results = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                UploadContext context = new UploadContext();
                context.setFile(file);
                context.setContentType(StorageType.MOVIE);
                context.setFileCategory(FileCategory.SUBTITLES);
                context.setMovieId(movieId);

                UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
                results.add(storageService.uploadFile(uploadRequest));
            }
        }
        return results;
    }

    public OriginalUploadMetadata uploadMovieOriginalVideo(MultipartFile file, String movieId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.MOVIE);
        context.setFileCategory(FileCategory.ORIGINAL);
        context.setMovieId(movieId);

        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public OriginalUploadMetadata uploadMovieTrailer(MultipartFile file, String movieId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.MOVIE);
        context.setFileCategory(FileCategory.TRAILER_ORIGINAL);
        context.setMovieId(movieId);

        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }



    public OriginalUploadMetadata uploadMovieTranscodeTrailer(MultipartFile file, String movieId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.MOVIE);
        context.setFileCategory(FileCategory.TRAILER_TRANSCODE);
        context.setMovieId(movieId);

        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public OriginalUploadMetadata uploadMovieTranscode(List<MultipartFile> files, String movieId) throws IOException {
        if (files == null || files.isEmpty()) {
            return null;
        }

        OriginalUploadMetadata lastMetadata = null;
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                UploadContext context = new UploadContext();
                context.setFile(file);
                context.setContentType(StorageType.MOVIE);
                context.setFileCategory(FileCategory.TRANSCODE);
                context.setMovieId(movieId);

                UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
                lastMetadata = storageService.uploadFile(uploadRequest);
            }
        }
        return lastMetadata;
    }

    // ==================== Series Upload Methods ====================

    public OriginalUploadMetadata uploadSeriesPoster(MultipartFile file, String seriesId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.SERIES);
        context.setFileCategory(FileCategory.POSTER);
        context.setSeriesId(seriesId);

        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public OriginalUploadMetadata uploadSeasonPoster(MultipartFile file, String seriesId, Integer seasonNumber) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.SEASON);
        context.setFileCategory(FileCategory.POSTER);
        context.setSeriesId(seriesId);
        context.setSeasonNumber(seasonNumber);

        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }


    public OriginalUploadMetadata uploadSeasonTrailer(MultipartFile file,String seriesId, Integer seasonNumber) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.SEASON);
        context.setFileCategory(FileCategory.TRAILER_ORIGINAL);
        context.setSeriesId(seriesId);
        context.setSeasonNumber(seasonNumber);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public OriginalUploadMetadata uploadSeasonTranscodeTrailer(MultipartFile file,String seriesId, Integer seasonNumber) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.SEASON);
        context.setFileCategory(FileCategory.TRAILER_TRANSCODE);
        context.setSeriesId(seriesId);
        context.setSeasonNumber(seasonNumber);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }


    public OriginalUploadMetadata uploadEpisodePoster(MultipartFile file, String seriesId, Integer seasonNumber, Integer episodeId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.EPISODE);
        context.setFileCategory(FileCategory.POSTER);
        context.setSeriesId(seriesId);
        context.setSeasonNumber(seasonNumber);
        context.setEpisodeNumber(episodeId);

        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public List<OriginalUploadMetadata> uploadEpisodeSubtitle(List<MultipartFile> files, String seriesId, Integer seasonNumber, Integer episodeId) throws IOException {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }
        List<OriginalUploadMetadata> results = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                UploadContext context = new UploadContext();
                context.setFile(file);
                context.setContentType(StorageType.EPISODE);
                context.setFileCategory(FileCategory.SUBTITLES);
                context.setSeriesId(seriesId);
                context.setSeasonNumber(seasonNumber);
                context.setEpisodeNumber(episodeId);
                UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
                results.add(storageService.uploadFile(uploadRequest));
            }
        }
        return results;
    }

    public OriginalUploadMetadata uploadEpisodeOriginalVideo(MultipartFile file, String seriesId, Integer seasonNumber, Integer episodeId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.EPISODE);
        context.setFileCategory(FileCategory.ORIGINAL);
        context.setSeriesId(seriesId);
        context.setSeasonNumber(seasonNumber);
        context.setEpisodeNumber(episodeId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public OriginalUploadMetadata uploadEpisodeTranscode(MultipartFile file, String seriesId, Integer seasonNumber, Integer episodeId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.EPISODE);
        context.setFileCategory(FileCategory.TRANSCODE);
        context.setSeriesId(seriesId);
        context.setSeasonNumber(seasonNumber);
        context.setEpisodeNumber(episodeId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    // ==================== TV Program Upload Methods ====================

    public OriginalUploadMetadata uploadTvProgramPoster(MultipartFile file, String tvProgramId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.TV_PROGRAMS);
        context.setFileCategory(FileCategory.POSTER);
        context.setTvProgramId(tvProgramId);

        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public OriginalUploadMetadata uploadTvProgramSeasonPoster(MultipartFile file, String tvProgramId, Integer seasonNumber) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.TV_PROGRAMS_SEASON);
        context.setFileCategory(FileCategory.POSTER);
        context.setTvProgramId(tvProgramId);
        context.setSeasonNumber(seasonNumber);

        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public OriginalUploadMetadata uploadTvProgramEpisodePoster(MultipartFile file, String tvProgramId, Integer seasonNumber, Integer episodeId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.TV_PROGRAMS_EPISODE);
        context.setFileCategory(FileCategory.POSTER);
        context.setTvProgramId(tvProgramId);
        context.setSeasonNumber(seasonNumber);
        context.setEpisodeNumber(episodeId);

        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public List<OriginalUploadMetadata> uploadTvProgramEpisodeSubtitle(List<MultipartFile> files, String tvProgramId, Integer seasonNumber, Integer episodeId) throws IOException {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }
        List<OriginalUploadMetadata> results = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                UploadContext context = new UploadContext();
                context.setFile(file);
                context.setContentType(StorageType.TV_PROGRAMS_EPISODE);
                context.setFileCategory(FileCategory.SUBTITLES);
                context.setTvProgramId(tvProgramId);
                context.setSeasonNumber(seasonNumber);
                context.setEpisodeNumber(episodeId);
                UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
                results.add(storageService.uploadFile(uploadRequest));
            }
        }
        return results;
    }

    public OriginalUploadMetadata uploadTvProgramEpisodeOriginalVideo(MultipartFile file, String tvProgramId, Integer seasonNumber, Integer episodeId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.TV_PROGRAMS_EPISODE);
        context.setFileCategory(FileCategory.ORIGINAL);
        context.setTvProgramId(tvProgramId);
        context.setSeasonNumber(seasonNumber);
        context.setEpisodeNumber(episodeId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public OriginalUploadMetadata uploadTvProgramEpisodeTranscode(MultipartFile file, String tvProgramId, Integer seasonNumber, Integer episodeId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.TV_PROGRAMS_EPISODE);
        context.setFileCategory(FileCategory.TRANSCODE);
        context.setTvProgramId(tvProgramId);
        context.setSeasonNumber(seasonNumber);
        context.setEpisodeNumber(episodeId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    // ==================== TV Channel Upload Methods ====================

    public OriginalUploadMetadata uploadTvChannelLogo(MultipartFile file, String channelId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.CHANNEL);
        context.setFileCategory(FileCategory.LOGO);
        context.setChannelId(channelId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    // ==================== Clip Upload Methods ====================

    public OriginalUploadMetadata uploadClipPoster(MultipartFile file, String clipId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.CLIP);
        context.setFileCategory(FileCategory.POSTER);
        context.setClipId(clipId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public OriginalUploadMetadata uploadClipVideo(MultipartFile file, String clipId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.CLIP);
        context.setFileCategory(FileCategory.ORIGINAL);
        context.setClipId(clipId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    public OriginalUploadMetadata uploadClipTranscode(MultipartFile file, String clipId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.CLIP);
        context.setFileCategory(FileCategory.TRANSCODE);
        context.setClipId(clipId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    // ==================== Actors Upload Methods ====================

    public OriginalUploadMetadata uploadActorPoster(MultipartFile file, String actorId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.ACTORS);
        context.setFileCategory(FileCategory.POSTER);
        context.setActorId(actorId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    // ==================== Actors Upload Methods ====================

    public OriginalUploadMetadata uploadTeamLogo(MultipartFile file, String teamId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.TEAM);
        context.setFileCategory(FileCategory.POSTER);
        context.setTeamId(teamId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }


    // ==================== Actors Upload Methods ====================

    public OriginalUploadMetadata uploadChampionLogo(MultipartFile file, String championId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.CHAMPION);
        context.setFileCategory(FileCategory.POSTER);
        context.setChampionId(championId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    // ==================== Ads Upload Methods ====================

    public OriginalUploadMetadata uploadAdPoster(MultipartFile file, String adId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.ADS);
        context.setFileCategory(FileCategory.POSTER);
        context.setAdId(adId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    // ==================== Banner Upload Methods ====================

    public OriginalUploadMetadata uploadBannerPoster(MultipartFile file, String bannerId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.BANNER);
        context.setFileCategory(FileCategory.POSTER);
        context.setBannerId(bannerId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    // ==================== Upcoming Match Upload Methods ====================

    public OriginalUploadMetadata uploadUpcomingMatchPoster(MultipartFile file, String upcomingMatchId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.UPCOMING_MATCH);
        context.setFileCategory(FileCategory.POSTER);
        context.setUpcomingMatchId(upcomingMatchId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }

    // ==================== Profile Upload Methods ====================

    public OriginalUploadMetadata uploadProfilePoster(MultipartFile file, String profileId) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        UploadContext context = new UploadContext();
        context.setFile(file);
        context.setContentType(StorageType.PROFILE);
        context.setFileCategory(FileCategory.POSTER);
        context.setProfileId(profileId);
        UploadRequest uploadRequest = uploadManager.buildUploadRequest(context);
        return storageService.uploadFile(uploadRequest);
    }


    // ==================== Remove Old File Method ====================

    public void removeOldFile(OriginalUploadMetadata file) {
        storageService.removeOldFile(file);
    }


    public void removeOldFile(List<OriginalUploadMetadata> files) {
        for (OriginalUploadMetadata file : files) {
            storageService.removeOldFile(file);
        }
    }


    public void removeOldFile(String filePath) {
        storageService.removeOldFile(filePath);
    }

    public void removeOldFile(Set<String> filesPath) {
        for (String filePath : filesPath) {
            storageService.removeOldFile(filePath);
        }
    }


    // ==================== Extract Images From Zip ====================

    public List<MultipartFile> extractImagesFromZip(MultipartFile zip) throws IOException {
        if (zip == null || zip.isEmpty()) {
            return null;
        }

        return storageService.extractImagesFromZip(zip);
    }

    // ==================== Parse Epg Xml ====================

    public List<ChannelEpgAdminRequest> parseEpgXml(MultipartFile xmlFile, String channelId) throws IOException {
        if (xmlFile == null || xmlFile.isEmpty() || channelId == null || channelId.isEmpty()) {
            return null;
        }

        return storageService.parseEpgXml(xmlFile, channelId);
    }
}
