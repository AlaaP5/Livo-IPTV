package com.rand.ishowApi.series.admin.domain.openSearch.model;

import com.rand.ishowApi.metadata.*;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

import java.util.List;

@Data
public class SeriesSearchDocument {

    private String id;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private OriginalUploadMetadata poster;
    private Badge badge;
    private List<TagMeta> tags;
    private List<ActorMeta> actors;
    private Integer releaseYear;

    private Integer seasonCount;

    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;

    private Boolean isPublish;
    private Boolean hasRight;
    private ContentLanguage language;
    private Boolean active;
    private Boolean isKids;
    private Boolean isSports;
    private Double rating;
}

