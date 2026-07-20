package com.rand.ishowApi.clip.admin.api.response;

import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ClipSectionAdminResponse {
    private String clipId;
    private String titleEn;
    private String titleAr;
    private OriginalUploadMetadata poster;
    private Timestamp createDate;
    private Boolean isTop;
    private Boolean isKids;
    private Boolean isSports;
    private Boolean isPublish;
    private Integer duration;
    private Badge badge;
    private AccessType accessType;
    private List<TagMeta> tags;
    private Long sectionId;
    private String sectionTitleAr;
    private String sectionTitleEn;
    private Integer sectionOrder;
    private boolean sectionActive;
    private boolean sectionPublish;
}


