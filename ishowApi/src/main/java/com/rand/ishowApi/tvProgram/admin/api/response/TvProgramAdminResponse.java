package com.rand.ishowApi.tvProgram.admin.api.response;

import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

import java.util.List;

@Data
public class TvProgramAdminResponse {
    private String tvProgramId;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private OriginalUploadMetadata poster;
    private Badge badge;
    private List<TagMeta> tags;
    private ContentLanguage language;
    private Boolean hasRight;
    private Boolean isKids;
    private Boolean active;
    private Boolean isSports;
    private Integer releaseYear;
    private java.util.List<ContentLanguage> subtitleLanguages;
    private java.util.List<ContentLanguage> audioLanguages;
    private Boolean isPublish;
}

