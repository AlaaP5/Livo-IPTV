package com.rand.ishowApi.channel.admin.domain.openSearch.model;

import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

import java.util.List;

@Data
public class ChannelSearchDocument {

    private String id;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private String streamUrl;
    private Badge badge;
    private OriginalUploadMetadata logo;
    private List<TagMeta> tags;

    private Boolean isPublish;
    private Boolean hasRight;
    private Boolean active;
    private Boolean isKids;
    private Boolean isSports;
}

