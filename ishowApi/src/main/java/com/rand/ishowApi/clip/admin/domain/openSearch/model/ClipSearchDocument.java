package com.rand.ishowApi.clip.admin.domain.openSearch.model;

import com.rand.ishowApi.metadata.*;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;

import lombok.Data;

import java.util.List;

@Data
public class ClipSearchDocument {

    private String id;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private OriginalUploadMetadata poster;
    private Badge badge;
    private List<TagMeta> tags;
    private AccessType accessType;
    private Integer duration;

    private TranscodeMetaData transcodeMetaData;
    private OriginalUploadMetadata originalUploadMetadata;

    private TranscodeStatus transcodeStatus;
    private Boolean isPublish;
    private Boolean hasRight;
    private Boolean active;
    private Boolean isKids;
    private Boolean isSports;

    protected String publishDate;
}

