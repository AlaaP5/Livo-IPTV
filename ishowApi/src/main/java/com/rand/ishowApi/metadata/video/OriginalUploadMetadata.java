package com.rand.ishowApi.metadata.video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OriginalUploadMetadata {
    private String fileName;
    private String generatedPath;
    private String fullPath;
    private Long size;
    private String contentType;
    private Integer height;
    private Integer width;
    private Integer bitrate;
    private String ext;
}
