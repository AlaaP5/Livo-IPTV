package com.rand.ishowApi.series.admin.api.response;

import com.rand.ishowApi.metadata.ActorMeta;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class SeriesSectionAdminResponse {
    private String seriesId;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private OriginalUploadMetadata poster;
    private Badge badge;
    private List<TagMeta> tags;
    private List<ActorMeta> actors;
    private Integer releaseYear;
    private Boolean isPublish;
    private Boolean hasRight;
    private ContentLanguage language;
    private Boolean active;
    private Boolean isKids;
    private Boolean isSports;
    private Double rating;
    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;
    private Boolean isTop;
    private Timestamp createDate;
    private Integer seasonCount;
    private Long sectionId;
    private String sectionTitleAr;
    private String sectionTitleEn;
    private Integer sectionOrder;
    private boolean sectionActive;
    private boolean sectionPublish;
}
