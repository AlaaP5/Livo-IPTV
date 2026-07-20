package com.rand.ishowApi.tvProgram.admin.domain.openSearch.model;

import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

import java.util.List;

@Data
public class TvProgramSearchDocument {

    private String id;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private OriginalUploadMetadata poster;
    private Badge badge;
    private List<TagMeta> tags;

    private ContentLanguage language;
    private Boolean active;
    private Boolean isPublish;
    private Boolean hasRight;
    private Boolean isKids;
    private Boolean isSports;

    private Integer releaseYear;
    private Integer seasonCount;

    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;
}

