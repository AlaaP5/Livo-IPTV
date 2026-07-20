package com.rand.ishowApi.metadata;


import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

@Data
public class ChannelMeta {

    private String id;
    private String titleEn;
    private String titleAr;
    private OriginalUploadMetadata logo;

}
