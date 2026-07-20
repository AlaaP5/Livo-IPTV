package com.rand.ishowApi.tvProgram.admin.api.response;

import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class TvProgramSectionAdminResponse {
    private String tvProgramId;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private OriginalUploadMetadata poster;
    private Badge badge;
    private List<TagMeta> tags;
    private Integer releaseYear;
    private Boolean isPublish;
    private Boolean hasRight;
    private ContentLanguage language;
    private Boolean active;
    private Boolean isKids;
    private Boolean isSports;
    private Boolean isTop;
    private Timestamp createDate;
    private Integer seasonCount;
    private java.util.List<ContentLanguage> subtitleLanguages;
    private java.util.List<ContentLanguage> audioLanguages;
    private Long sectionId;
    private String sectionTitleAr;
    private String sectionTitleEn;
    private Integer sectionOrder;
    private boolean sectionActive;
    private boolean sectionPublish;
}

