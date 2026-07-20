package com.rand.ishowApi.storage.service;

import com.rand.ishowApi.channel.admin.api.request.ChannelEpgAdminRequest;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.storage.helper.StoragePathHelper;
import com.rand.ishowApi.storage.model.contentType.StorageType;
import com.rand.ishowApi.storage.model.contentType.ContentTypeIds;
import com.rand.ishowApi.storage.model.contentType.FileCategory;
import com.rand.ishowApi.storage.model.request.UploadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.jspecify.annotations.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


@Service
@RequiredArgsConstructor
public class StorageService {

    @Value("${storage.base-dir}")
    private String basePath;

    private final StoragePathHelper storagePathHelper;

    // ==================== Public API ====================


    public void removeOldFile(OriginalUploadMetadata file) {
        try {
            if (file == null || file.getFullPath() == null) {
                return;
            }
            String filePath = file.getFullPath();
            storagePathHelper.moveOldFiles(List.of(filePath));
        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.STORAGE_FILE_REMOVE_FAILED,null);
        }
    }

    public void removeOldFile(String filePath) {
        try {
            if (filePath == null ) {
                return;
            }
            storagePathHelper.moveOldFiles(List.of(filePath));
        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.STORAGE_FILE_REMOVE_FAILED,null);
        }
    }

    public List<ChannelEpgAdminRequest> parseEpgXml(MultipartFile xmlFile, String channelId) {
        if (xmlFile == null || xmlFile.isEmpty()) {
            throw new CustomException(ApiResponseCode.ERROR_FILE_EMPTY, null);
        }
        if (channelId == null || channelId.isBlank()) {
            throw new CustomException(ApiResponseCode.STORAGE_CHANNEL_ID_REQUIRED, null);
        }

        try (InputStream inputStream = xmlFile.getInputStream()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            configureSecureXmlFactory(factory);

            Document document = factory.newDocumentBuilder().parse(inputStream);
            document.getDocumentElement().normalize();

            NodeList programmes = document.getElementsByTagName("programme");
            List<ChannelEpgAdminRequest> responses = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

            for (int i = 0; i < programmes.getLength(); i++) {
                Element programme = (Element) programmes.item(i);

                String start = programme.getAttribute("start");
                String stop = programme.getAttribute("stop");
                if (start.isBlank() || stop.isBlank()) {
                    continue;
                }

                ChannelEpgAdminRequest request = new ChannelEpgAdminRequest();
                request.setChannelId(channelId);
                request.setStartDate(LocalDateTime.parse(start, formatter));
                request.setEndDate(LocalDateTime.parse(stop, formatter));
                request.setTitleAr(programme.getElementsByTagName("title").item(0).getTextContent());
                request.setTitleEn(programme.getElementsByTagName("title").item(0).getTextContent());
                responses.add(request);
            }

            return responses;
        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.ERROR_FILE_READ, null);
        }
    }

    public OriginalUploadMetadata uploadFile(UploadRequest request) throws IOException {
        validateUploadRequest(request);
        String storagePath = resolvePath(request);
        String extension = getExtension(request.getFile().getOriginalFilename());
        String fileName = extractFileName(extension,storagePath );
        Path fullPath = Paths.get(basePath,storagePath,fileName);
        Path generatedPath = Paths.get(storagePath,fileName);
        Files.createDirectories(fullPath.getParent());
        try (InputStream inputStream = request.getFile().getInputStream()) {
            Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);
        }
        return OriginalUploadMetadata.builder()
                .fileName(fileName)
                .generatedPath(toRelativePath(generatedPath.toString()))
                .fullPath(toRelativePath(fullPath.toAbsolutePath().toString()))
                .size(request.getFile().getSize())
                .ext(extension)
                .contentType(request.getContentType().name())
                .build();
    }

    public List<MultipartFile> extractImagesFromZip(MultipartFile zip) {
        validateZipUploadRequest(zip);

        try (InputStream inputStream = zip.getInputStream();
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {

            List<MultipartFile> extractedFiles = new ArrayList<>();
            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {
                try {
                    if (entry.isDirectory()) {
                        continue;
                    }

                    String fileName = resolveZipEntryFileName(entry.getName());
                    if (!isSupportedImage(fileName)) {
                        continue;
                    }

                    extractedFiles.add(new InMemoryMultipartFile(
                            fileName,
                            fileName,
                            resolveImageContentType(fileName),
                            readCurrentZipEntry(zipInputStream)
                    ));
                } finally {
                    zipInputStream.closeEntry();
                }
            }

            return extractedFiles;
        } catch (IOException e) {
            throw new CustomException(ApiResponseCode.STORAGE_ARCHIVE_READ_FAILED, null);
        }
    }

    private String resolvePath(UploadRequest request) {

        StorageType type = request.getContentType();
        FileCategory category = request.getFileCategory();
        ContentTypeIds ids = request.getContentTypeIds();

        return switch (type) {

            case MOVIE -> storagePathHelper.buildMoviePath(ids.getMovieId(), category);

            case SERIES -> storagePathHelper.buildSeriesPath(ids.getSeriesId(), category);

            case SEASON -> storagePathHelper.buildSeasonPath(
                    ids.getSeriesId(),
                    ids.getSeasonNumber(),
                    category
            );

            case EPISODE -> storagePathHelper.buildEpisodePath(
                    ids.getSeriesId(),
                    ids.getSeasonNumber(),
                    ids.getEpisodeNumber(),
                    category
            );

            case TV_PROGRAMS -> storagePathHelper.buildTvPath(ids.getTvProgramId(), category);
            case TV_PROGRAMS_SEASON -> storagePathHelper.buildTvSeasonPath(
                    ids.getTvProgramId(),
                    ids.getSeasonNumber(),
                    category
            );
            case TV_PROGRAMS_EPISODE -> storagePathHelper.buildEpisodePath(
                    ids.getTvProgramId(),
                    ids.getSeasonNumber(),
                    ids.getEpisodeNumber(),
                    category
            );

            case CHANNEL -> storagePathHelper.buildChannelPath(ids.getChannelId(), category);

            case CLIP -> storagePathHelper.buildClipPath(ids.getClipId(), category);

            case ACTORS->storagePathHelper.buildActorPath(ids.getActorId(),category);
            case TEAM->storagePathHelper.buildTeamPath(ids.getTeamId(),category);
            case CHAMPION->storagePathHelper.buildChampionPath(ids.getChampionId(),category);
            case ADS->storagePathHelper.buildAdsPath(ids.getAdsId(),category);
            case BANNER->storagePathHelper.buildBannerPath(ids.getBannerId(),category);
            case UPCOMING_MATCH->storagePathHelper.buildUpComingMatchPath(ids.getUpComingMatchId(),category);
            case PROFILE->storagePathHelper.buildProfilePath(ids.getActorId(),category);
        };
    }

    private void validateContentTypeFields(StorageType type, ContentTypeIds ids) {

        switch (type) {

            case MOVIE -> require(ids.getMovieId(), ApiResponseCode.STORAGE_MOVIE_ID_REQUIRED);

            case SERIES -> require(ids.getSeriesId(), ApiResponseCode.STORAGE_SERIES_ID_REQUIRED);

            case SEASON -> {
                require(ids.getSeriesId(), ApiResponseCode.STORAGE_SERIES_ID_REQUIRED);

                if (ids.getSeasonNumber() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_SEASON_NUMBER_REQUIRED, null);
                }
            }

            case EPISODE -> {
                require(ids.getSeriesId(), ApiResponseCode.STORAGE_SERIES_ID_REQUIRED);

                if (ids.getSeasonNumber() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_SEASON_NUMBER_REQUIRED, null);
                }

                if (ids.getEpisodeNumber() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_EPISODE_NUMBER_REQUIRED, null);
                }
            }

            case TV_PROGRAMS -> require(ids.getTvProgramId(), ApiResponseCode.STORAGE_TV_PROGRAM_ID_REQUIRED);

            case TV_PROGRAMS_SEASON -> {
                require(ids.getTvProgramId(), ApiResponseCode.STORAGE_TV_PROGRAM_ID_REQUIRED);

                if (ids.getSeasonNumber() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_SEASON_NUMBER_REQUIRED, null);
                }
            }

            case TV_PROGRAMS_EPISODE -> {
                require(ids.getTvProgramId(), ApiResponseCode.STORAGE_TV_PROGRAM_ID_REQUIRED);

                if (ids.getSeasonNumber() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_SEASON_NUMBER_REQUIRED, null);
                }

                if (ids.getEpisodeNumber() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_EPISODE_NUMBER_REQUIRED, null);
                }
            }

            case CHANNEL -> require(ids.getChannelId(), ApiResponseCode.STORAGE_CHANNEL_ID_REQUIRED);

            case CLIP -> require(ids.getClipId(), ApiResponseCode.STORAGE_CLIP_ID_REQUIRED);
        }
    }

    private String extractFileName(String ext, String path) {
        String fileName;
        Path fullPath;
        do {
            String uuid = UUID.randomUUID().toString().substring(0, 8);
            fileName = uuid + "." + ext;
            fullPath = Paths.get(path, fileName);

        } while (Files.exists(fullPath));
        return fileName;
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new CustomException(ApiResponseCode.INVALID_FILE_EXTENSION, null);
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    private void configureSecureXmlFactory(DocumentBuilderFactory factory) throws ParserConfigurationException {
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
    }

    private void require(String value, String errorCode) {
        if (value == null || value.isBlank()) {
            throw new CustomException(errorCode, null);
        }
    }
    private void validateUploadRequest(UploadRequest request) {

        if (request.getContentType() == null) {
            throw new CustomException(ApiResponseCode.STORAGE_CONTENT_TYPE_REQUIRED, null);
        }

        if (request.getContentTypeIds() == null) {
            throw new CustomException(ApiResponseCode.STORAGE_CONTENT_IDS_REQUIRED, null);
        }

        if (request.getFileCategory() == null) {
            throw new CustomException(ApiResponseCode.STORAGE_FILE_CATEGORY_REQUIRED, null);
        }
        validateContentTypeFields(request.getContentType(), request.getContentTypeIds());
    }

    private void validateZipUploadRequest(MultipartFile zip) {
        if (zip == null || zip.isEmpty()) {
            throw new CustomException(ApiResponseCode.STORAGE_ARCHIVE_EMPTY, null);
        }

        String originalFilename = zip.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase(Locale.ROOT).endsWith(".zip")) {
            throw new CustomException(ApiResponseCode.STORAGE_ARCHIVE_TYPE_INVALID, null);
        }
    }

    private byte[] readCurrentZipEntry(ZipInputStream zipInputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        zipInputStream.transferTo(outputStream);
        return outputStream.toByteArray();
    }

    private boolean isSupportedImage(String fileName) {
        String extension = getFileExtension(fileName);
        return switch (extension) {
            case "jpg", "jpeg", "png", "gif", "webp" -> true;
            default -> false;
        };
    }

    private String resolveImageContentType(String fileName) {
        return switch (getFileExtension(fileName)) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            default -> throw new CustomException(ApiResponseCode.STORAGE_ARCHIVE_NO_IMAGES, null);
        };
    }

    private String resolveZipEntryFileName(String entryName) {
        if (entryName == null || entryName.isBlank()) {
            return "";
        }

        String normalized = entryName.replace("\\", "/");
        int lastSlashIndex = normalized.lastIndexOf('/');
        return lastSlashIndex >= 0 ? normalized.substring(lastSlashIndex + 1) : normalized;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }

    private static class InMemoryMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        private InMemoryMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content == null ? new byte[0] : content;
        }

        @Override
        public @NonNull String getName() {
            return name;
        }

        @Override
        public @NonNull String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public @NonNull String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte @NonNull [] getBytes() {
            return content.clone();
        }

        @Override
        public @NonNull InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(@NonNull File dest) throws IllegalStateException {
            throw new UnsupportedOperationException("In-memory multipart files cannot be written to disk");
        }

        public void transferTo(@NonNull Path dest) throws IllegalStateException {
            throw new UnsupportedOperationException("In-memory multipart files cannot be written to disk");
        }
    }

    public static String toRelativePath(String path){
        return path.replace("\\", "/")          // Replace backslashes with forward slashes
                .replaceFirst("^\\.?/", "");
    }




}
