package com.rand.ishowApi.metadata.video;


import lombok.Data;

@Data
public class VideoVariant {
    private String resolution;
    private int width;
    private int height;
    private int bitrate;
    private String playlistPath;
    private String segmentPath;
}
