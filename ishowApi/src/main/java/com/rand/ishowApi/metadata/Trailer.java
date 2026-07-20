package com.rand.ishowApi.metadata;


import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import lombok.Data;

@Data
public class Trailer {
    private TranscodeMetaData transcodeMetaData;
    private OriginalUploadMetadata originalUploadMetadata;
    private TranscodeStatus transcodeStatus;
    private Boolean isPublish;

}
