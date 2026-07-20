package com.rand.ishowApi.clip.admin.api.response;

import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

import java.util.List;
@Data
public class ClipsAdminResponse {
    private String clipId;
    private String titleEn;
    private String titleAr;
    private OriginalUploadMetadata poster;
    private Integer duration;
    private Badge badge;
    private AccessType accessType;
    private List<TagMeta> tags;
    private Boolean hasRight;
    private Boolean isKids;
    private Boolean active;
    private Boolean isSports;
    private TranscodeStatus transcodeStatus;
    private Boolean isPublish;
}
