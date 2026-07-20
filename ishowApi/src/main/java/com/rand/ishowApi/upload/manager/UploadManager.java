package com.rand.ishowApi.upload.manager;


import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.storage.model.contentType.ContentTypeIds;
import com.rand.ishowApi.storage.model.request.UploadRequest;
import com.rand.ishowApi.upload.model.UploadContext;
import org.springframework.stereotype.Component;

@Component
public class UploadManager {

    public UploadRequest buildUploadRequest(UploadContext context) {
        UploadRequest request = new UploadRequest();
        request.setFile(context.getFile());
        request.setContentType(context.getContentType());
        request.setFileCategory(context.getFileCategory());

        ContentTypeIds contentTypeIds = new ContentTypeIds();

        switch (context.getContentType()) {
            case MOVIE -> {
                if (context.getMovieId() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_CONTENT_TYPE_REQUIRED, null);
                }
                contentTypeIds.setMovieId(context.getMovieId());
            }

            case SERIES -> {
                if (context.getSeriesId() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_SERIES_ID_REQUIRED, null);
                }
                contentTypeIds.setSeriesId(context.getSeriesId());
            }

            case SEASON -> {
                if (context.getSeriesId() == null || context.getSeasonNumber() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_SEASON_NUMBER_REQUIRED, null);
                }
                contentTypeIds.setSeriesId(context.getSeriesId());
                contentTypeIds.setSeasonNumber(context.getSeasonNumber());
            }

            case EPISODE -> {
                if (context.getSeriesId() == null || context.getSeasonNumber() == null || context.getEpisodeNumber() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_EPISODE_NUMBER_REQUIRED, null);
                }
                contentTypeIds.setSeriesId(context.getSeriesId());
                contentTypeIds.setSeasonNumber(context.getSeasonNumber());
                contentTypeIds.setEpisodeNumber(context.getEpisodeNumber());
            }

            case TV_PROGRAMS -> {
                if (context.getTvProgramId() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_TV_PROGRAM_ID_REQUIRED, null);
                }
                contentTypeIds.setTvProgramId(context.getTvProgramId());
            }

            case TV_PROGRAMS_SEASON -> {
                if (context.getTvProgramId() == null || context.getSeasonNumber() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_TV_PROGRAM_ID_REQUIRED, null);
                }
                contentTypeIds.setTvProgramId(context.getTvProgramId());
                contentTypeIds.setSeasonNumber(context.getSeasonNumber());
            }

            case TV_PROGRAMS_EPISODE -> {
                if (context.getTvProgramId() == null || context.getSeasonNumber() == null || context.getEpisodeNumber() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_TV_PROGRAM_ID_REQUIRED, null);
                }
                contentTypeIds.setTvProgramId(context.getTvProgramId());
                contentTypeIds.setSeasonNumber(context.getSeasonNumber());
                contentTypeIds.setEpisodeNumber(context.getEpisodeNumber());
            }

            case CHANNEL -> {
                if (context.getChannelId() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_CHANNEL_ID_REQUIRED,null);
                }
                contentTypeIds.setChannelId(context.getChannelId());
            }

            case CLIP -> {
                if (context.getClipId() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_CLIP_ID_REQUIRED, null);
                }
                contentTypeIds.setClipId(context.getClipId());
            }

            case ACTORS -> {
                if (context.getActorId() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_ACTOR_ID_REQUIRED, null);
                }
                contentTypeIds.setActorId(context.getActorId());
            }

            case ADS -> {
                if (context.getAdId() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_AD_ID_REQUIRED, null);
                }
                contentTypeIds.setAdsId(context.getAdId());
            }

            case TEAM -> {
                if (context.getTeamId() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_TEAM_ID_REQUIRED, null);
                }
                contentTypeIds.setTeamId(context.getTeamId());
            }

            case CHAMPION -> {
                if (context.getChampionId() == null) {
                    throw new CustomException(ApiResponseCode.STORAGE_CHAMPION_ID_REQUIRED, null);
                }
                contentTypeIds.setChampionId(context.getChampionId());
            }
        }

        request.setContentTypeIds(contentTypeIds);
        return request;
    }

}
