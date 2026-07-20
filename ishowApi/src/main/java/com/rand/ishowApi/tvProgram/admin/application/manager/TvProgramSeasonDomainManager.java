package com.rand.ishowApi.tvProgram.admin.application.manager;

import com.rand.ishowApi.metadata.Trailer;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.tvProgram.admin.api.request.AddTvProgramSeasonAdminRequest;
import com.rand.ishowApi.tvProgram.admin.api.request.UpdateTvProgramSeasonAdminRequest;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramSeason;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

@Component
public class TvProgramSeasonDomainManager {
    public TvProgramSeason createSeason(AddTvProgramSeasonAdminRequest request, OriginalUploadMetadata poster) {
        TvProgramSeason season = new TvProgramSeason();
        season.setTvProgramId(request.getTvProgramId());
        season.setSeasonNumber(request.getSeasonNumber());
        season.setTitleEn(request.getTitleEn());
        season.setTitleAr(request.getTitleAr());
        season.setDescriptionEn(request.getDescriptionEn());
        season.setDescriptionAr(request.getDescriptionAr());
        season.setReleaseYear(request.getReleaseYear());
        season.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        season.setPoster(poster);
        return season;
    }

    public void updateSeason(TvProgramSeason season, UpdateTvProgramSeasonAdminRequest request, OriginalUploadMetadata poster) {
        if (request.getSeasonNumber() != null) {
            season.setSeasonNumber(request.getSeasonNumber());
        }
        if (request.getTitleEn() != null) {
            season.setTitleEn(request.getTitleEn());
        }
        if (request.getTitleAr() != null) {
            season.setTitleAr(request.getTitleAr());
        }
        if (request.getDescriptionEn() != null) {
            season.setDescriptionEn(request.getDescriptionEn());
        }
        if (request.getDescriptionAr() != null) {
            season.setDescriptionAr(request.getDescriptionAr());
        }
        if (request.getReleaseYear() != null) {
            season.setReleaseYear(request.getReleaseYear());
        }
        if (request.getActive() != null) {
            season.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        }
        if (poster != null) {
            season.setPoster(poster);
        }
    }

    public void addSeasonFiles(TvProgramSeason season, OriginalUploadMetadata trailer) {
        Trailer seasonTrailer = season.getTrailer() != null ? season.getTrailer() : new Trailer();
        seasonTrailer.setOriginalUploadMetadata(trailer);
        seasonTrailer.setIsPublish(false);
        seasonTrailer.setTranscodeStatus(TranscodeStatus.PENDING);
        season.setTrailer(seasonTrailer);
    }

    public void updateTvProgramSeasonCount(TvProgram tvProgram, Integer seasonCount) {
        tvProgram.setSeasonCount(seasonCount);
    }
}

