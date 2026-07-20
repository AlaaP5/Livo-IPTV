package com.rand.ishowApi.channel.admin.api.response;

import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

import java.util.List;

@Data
public class ChannelAdminFilterResponse {

    private String id;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private Badge badge;
    private List<TagMeta> tags;
    private OriginalUploadMetadata logo;
    private Boolean hasRight;
    private Boolean isKids;
    private Boolean active;
    private Boolean isSports;
    private Boolean isPublish;
}
