package com.rand.ishowApi.channel.admin.domain.openSearch.model;

import com.rand.ishowApi.metadata.*;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.openSearch.model.SectionDocument;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ChannelDocument extends SectionDocument {
    private String channelId;
    private String titleEn;
    private String titleAr;
    private OriginalUploadMetadata logo;
    private Badge badge;
    private List<TagMeta> tags;
    private Boolean isPublish;
    private Boolean hasRight;
    private Boolean active;
    private Boolean isKids;
    private Boolean isSports;
    private Boolean isTop;
    private Timestamp createDate;
}

