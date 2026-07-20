package com.rand.ishowApi.storage.helper;

import com.rand.ishowApi.storage.model.contentType.FileCategory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class StoragePathHelper {



    @Value("${storage.deleted-dir}")
    private String deletePath;

    // ==================== Content Paths ====================

    public String buildMoviePath(String movieId, FileCategory category) {


        String base = String.format("/movies/%s", movieId);

        return switch (category) {
            case ORIGINAL -> base + "/original/";
            case TRANSCODE -> base + "/transcoded/";
            case POSTER -> base + "/poster/";
            case SUBTITLES -> base + "/subtitles/";
            case TRAILER_ORIGINAL -> base + "/trailer/original/";
            case TRAILER_TRANSCODE -> base + "/trailer/transcoded/";
            case  LOGO  ->
                    throw new IllegalArgumentException("Invalid category for movie: " + category);
        };
    }
    public String buildSeriesPath(String seriesId, FileCategory category) {

        Objects.requireNonNull(category, "category is required for series path");

        String base = String.format("/series/%s", seriesId);

        return switch (category) {
            case POSTER -> base + "/poster/";
            case TRAILER_ORIGINAL -> base + "/trailer/original/";
            case TRAILER_TRANSCODE -> base + "/trailer/transcoded/";
            default -> throw new IllegalArgumentException("Invalid category for series: " + category);
        };
    }
    public String buildSeasonPath(String seriesId, Integer season, FileCategory category) {

        Objects.requireNonNull(category, "category is required for season path");

        String base = String.format("/series/%s/seasons/%d"
                , seriesId, season);

        return switch (category) {
            case POSTER -> base + "/poster/";
            case TRAILER_ORIGINAL -> base + "/trailer/original/";
            case TRAILER_TRANSCODE -> base + "/trailer/transcoded/";
            default -> throw new IllegalArgumentException("Invalid category for season: " + category);
        };
    }
    public String buildEpisodePath(String seriesId, Integer season, Integer episode, FileCategory category) {


        String base = String.format("/series/%s/seasons/%d/episodes/%d"
                , seriesId, season, episode);

        return switch (category) {
            case ORIGINAL -> base + "/original/";
            case TRANSCODE -> base + "/transcoded/";
            case  SUBTITLES -> base + "/subtitles/";
            case TRAILER_ORIGINAL -> base + "/trailer/original/";
            case TRAILER_TRANSCODE -> base + "/trailer/transcoded/";            case POSTER -> base + "/poster/";
            default -> throw new IllegalArgumentException("Invalid category for episode: " + category);
        };
    }
    public String buildTvPath(String tvId, FileCategory category) {

        Objects.requireNonNull(category, "category is required for tv path");

        String base = String.format("/tv/%s", tvId);

        return switch (category) {
            case POSTER -> base + "/poster/";
            case TRAILER_ORIGINAL -> base + "/trailer/original/";
            case TRAILER_TRANSCODE -> base + "/trailer/transcoded/";            default -> throw new IllegalArgumentException("Invalid category for TV: " + category);
        };
    }
    public String buildTvSeasonPath(String tvId, Integer season, FileCategory category) {

        Objects.requireNonNull(category, "category is required for tvSeason path");

        String base = String.format("/tv/%s/seasons/%d"
                , tvId, season);
        return switch (category) {
            case POSTER -> base + "/poster/";
            case TRAILER_ORIGINAL -> base + "/trailer/original/";
            case TRAILER_TRANSCODE -> base + "/trailer/transcoded/";            default -> throw new IllegalArgumentException("Invalid category for season: " + category);
        };
    }
    public String buildTvEpisodePath(String tvId, Integer season, Integer episode, FileCategory category) {


        String base = String.format("/tv/%s/seasons/%d/episodes/%d"
                , tvId, season, episode);

        return switch (category) {
            case ORIGINAL -> base + "/original/";
            case TRANSCODE -> base + "/transcoded/";
            case  SUBTITLES -> base + "/subtitles/";
            case TRAILER_ORIGINAL -> base + "/trailer/original/";
            case TRAILER_TRANSCODE -> base + "/trailer/transcoded/";            case POSTER -> base + "/poster/";
            default -> throw new IllegalArgumentException("Invalid category for episode: " + category);
        };
    }
    public String buildChannelPath(String channelId, FileCategory category) {

        Objects.requireNonNull(category, "category is required for channel path");

        String base = String.format("/channels/%s", channelId);

        return switch (category) {
            case LOGO -> base + "/logo/";
            default -> throw new IllegalArgumentException("Invalid category for channel: " + category);
        };
    }
    public String buildClipPath(String clipId, FileCategory category) {

        Objects.requireNonNull(category, "category is required for clip path");

        String base = String.format("/clips/%s", clipId);

        return switch (category) {
            case ORIGINAL -> base + "/original/";
            case TRANSCODE -> base + "/transcoded/";
            case POSTER -> base + "/poster/";
            default -> throw new IllegalArgumentException("Invalid category for clip: " + category);
        };
    }


    public String buildActorPath(String actorId, FileCategory category) {

        String base = String.format("/actors/%s", actorId);

        return switch (category) {
            case POSTER -> base + "/poster/";
            default -> throw new IllegalArgumentException("Invalid category for actor: " + category);
        };
    }

    public String buildAdsPath(String adsId, FileCategory category) {

        String base = String.format("/Ads/%s", adsId);

        return switch (category) {
            case POSTER -> base + "/poster/";
            default -> throw new IllegalArgumentException("Invalid category for ads: " + category);
        };
    }

    public String buildBannerPath(String bannerId, FileCategory category) {

        String base = String.format("/Banner/%s", bannerId);

        return switch (category) {
            case POSTER -> base + "/poster/";
            default -> throw new IllegalArgumentException("Invalid category for Banner " + category);
        };
    }

    public String buildUpComingMatchPath(String matchId, FileCategory category) {

        String base = String.format("/match/%s", matchId);

        return switch (category) {
            case POSTER -> base + "/poster/";
            default -> throw new IllegalArgumentException("Invalid category for upcoming match " + category);
        };
    }

    public String buildTeamPath(String teamId, FileCategory category) {

        String base = String.format("/team/%s", teamId);

        return switch (category) {
            case POSTER -> base + "/poster/";
            default -> throw new IllegalArgumentException("Invalid category for team: " + category);
        };
    }


    public String buildChampionPath(String championId, FileCategory category) {

        String base = String.format("/champion/%s", championId);

        return switch (category) {
            case POSTER -> base + "/poster/";
            default -> throw new IllegalArgumentException("Invalid category for champion: " + category);
        };
    }

    public String buildProfilePath(String profileId, FileCategory category) {

        String base = String.format("/profile/%s", profileId);

        return switch (category) {
            case POSTER -> base + "/poster/";
            default -> throw new IllegalArgumentException("Invalid category for profile " + category);
        };
    }



    // ====================Helper method====================

    public static String sanitizeName(String input) {
        if (input == null || input.isBlank()) return "file";

        return input.toLowerCase()
                .replaceAll("[^a-z0-9]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
    public File convertMultipartToFile(MultipartFile multipartFile) throws IOException {

        // ✅ Safe path creation
        Path tempDirPath = Paths.get(System.getProperty("java.io.tmpdir"), "ishow-upload-temp")
                .toAbsolutePath()
                .normalize();
        // ✅ Ensure directory exists
        Files.createDirectories(tempDirPath);

        // ✅ Sanitize filename
        String safeName = sanitizeName(multipartFile.getOriginalFilename());

        // ✅ Create file path
        Path tempFilePath = tempDirPath.resolve("upload-" + safeName);

        File tempFile = tempFilePath.toFile();

        // ✅ Save file
        multipartFile.transferTo(tempFile);

        return tempFile;
    }




    // ====================deleted method====================
    public void moveOldFiles(List<String> Files) throws IOException {
        Path targetDir = prepareDeletedDirectory();
        List<Path> paths = Files.stream()
                .map(Paths::get)
                .toList();
        for (Path sourcePath : paths) {
            moveIfExists(sourcePath, targetDir);
        }
    }

    private Path prepareDeletedDirectory() throws IOException {
        Path targetDir =Paths.get(deletePath).toAbsolutePath().normalize().resolve("");
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }
        return targetDir;
    }

    private void moveIfExists(Path source ,Path targetDir) throws IOException {

        Path targetPath = targetDir.resolve(source.getFileName());
        if (Files.exists(source)) {
            Files.move(source, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }



}






