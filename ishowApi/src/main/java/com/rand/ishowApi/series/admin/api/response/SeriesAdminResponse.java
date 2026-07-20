package com.rand.ishowApi.series.admin.api.response;

import com.rand.ishowApi.metadata.ActorMeta;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

import java.util.List;

@Data
public class SeriesAdminResponse {
    private String seriesId;
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
    private Double rate;
    private Integer releaseYear;
    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;
    private Boolean isPublish;
}

