package com.rand.ishowApi.clip.admin.domain.openSearch.model;

import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.openSearch.model.SectionDocument;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ClipDocument extends SectionDocument {
    private String clipId;
    private String titleEn;
    private String titleAr;
    private OriginalUploadMetadata poster;
    private Integer duration;
    private Badge badge;
    private List<TagMeta> tags;
    private AccessType accessType;
    private TranscodeMetaData transcodeMetaData;
    private OriginalUploadMetadata originalUploadMetadata;
    private TranscodeStatus transcodeStatus;
    private Boolean isPublish;
    private Boolean hasRight;
    private Boolean active;
    private Boolean isKids;
    private Boolean isSports;
    private Boolean isTop;
    private Timestamp createDate;
    private String publishDate;
}


