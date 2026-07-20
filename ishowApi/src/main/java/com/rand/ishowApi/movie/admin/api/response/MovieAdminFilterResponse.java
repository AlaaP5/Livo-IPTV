package com.rand.ishowApi.movie.admin.api.response;


import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

import java.util.List;


@Data
public class MovieAdminFilterResponse {
    private String movieId;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private OriginalUploadMetadata poster;
    private Integer releaseYear;
    private Integer duration;
    private Double rating;
    private Badge badge;
    private ContentLanguage language;
    private List<TagMeta> tags;
    private AccessType accessType;
    private Boolean hasRight;
    private Boolean isKids;
    private Boolean active;
    private Boolean isSports;
    private TranscodeStatus transcodeStatus;
    private Boolean isPublish;
    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;


}
