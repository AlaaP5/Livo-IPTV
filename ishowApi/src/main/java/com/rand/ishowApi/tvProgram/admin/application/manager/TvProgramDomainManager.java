package com.rand.ishowApi.tvProgram.admin.application.manager;

import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.tvProgram.admin.api.request.AddTvProgramAdminRequest;
import com.rand.ishowApi.tvProgram.admin.api.request.UpdateTvProgramAdminRequest;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TvProgramDomainManager {

    // ================================================
    // =========== TvProgram
    // =================================================

    public TvProgram create(AddTvProgramAdminRequest req, List<TagMeta> tags) {
        return TvProgram.builder()
                .titleEn(req.getTitleEn())
                .titleAr(req.getTitleAr())
                .descriptionEn(req.getDescriptionEn())
                .descriptionAr(req.getDescriptionAr())
                .releaseYear(req.getReleaseYear())
                .badge(req.getBadge())
                .tags(tags)
                .hasRight(BooleanConverter.getActiveBoolean(req.getHasRight()))
                .language(req.getLanguage())
                .isKids(BooleanConverter.getActiveBoolean(req.getIsKids()))
                .isSports(BooleanConverter.getActiveBoolean(req.getIsSports()))
                .subtitleLanguages(req.getSubtitleLanguages())
                .audioLanguages(req.getAudioLanguages())
                .isPublish(false)
                .active(BooleanConverter.getActiveBoolean(req.getActive()))
                .build();
    }

    public void addTvProgramPoster(TvProgram tvProgram, OriginalUploadMetadata poster) {
        tvProgram.setPoster(poster);
    }

    public void update(
            TvProgram tvProgram,
            UpdateTvProgramAdminRequest request,
            List<TagMeta> tags,
            OriginalUploadMetadata poster
    ) {
        if (request.getTitleEn() != null) {
            tvProgram.setTitleEn(request.getTitleEn());
        }
        if (request.getTitleAr() != null) {
            tvProgram.setTitleAr(request.getTitleAr());
        }
        if (request.getDescriptionEn() != null) {
            tvProgram.setDescriptionEn(request.getDescriptionEn());
        }
        if (request.getDescriptionAr() != null) {
            tvProgram.setDescriptionAr(request.getDescriptionAr());
        }
        if (request.getBadge() != null) {
            tvProgram.setBadge(request.getBadge());
        }
        if (request.getLanguage() != null) {
            tvProgram.setLanguage(request.getLanguage());
        }
        if (request.getSubtitleLanguages() != null) {
            tvProgram.setSubtitleLanguages(request.getSubtitleLanguages());
        }
        if (request.getAudioLanguages() != null) {
            tvProgram.setAudioLanguages(request.getAudioLanguages());
        }
        if (request.getHasRight() != null) {
            tvProgram.setHasRight(BooleanConverter.getActiveBoolean(request.getHasRight()));
        }
        if (request.getIsKids() != null) {
            tvProgram.setIsKids(BooleanConverter.getActiveBoolean(request.getIsKids()));
        }
        if (request.getIsSports() != null) {
            tvProgram.setIsSports(BooleanConverter.getActiveBoolean(request.getIsSports()));
        }
        if (request.getReleaseYear() != null) {
            tvProgram.setReleaseYear(request.getReleaseYear());
        }
        if (tags != null) {
            tvProgram.setTags(tags);
        }
        if (request.getActive() != null) {
            tvProgram.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        }
        if (poster != null) {
            tvProgram.setPoster(poster);
        }
    }
}

