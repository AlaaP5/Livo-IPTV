package com.rand.ishowApi.upload.model;

import com.rand.ishowApi.storage.model.contentType.StorageType;
import com.rand.ishowApi.storage.model.contentType.FileCategory;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadContext {

        MultipartFile file;
        StorageType contentType;
        FileCategory fileCategory;
        String movieId;
        String seriesId;
        Integer seasonNumber;
        Integer episodeNumber;
        String tvProgramId;
        String channelId;
        String clipId;
        String actorId;
        String adId;
        String bannerId;
        String upcomingMatchId;
        String teamId;
        String championId;
        String profileId;

}
