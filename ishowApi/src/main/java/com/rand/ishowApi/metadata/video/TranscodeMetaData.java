package com.rand.ishowApi.metadata.video;

import lombok.Data;

import java.util.List;

@Data
public class TranscodeMetaData {
    private String masterPlaylist;
    private List<VideoVariant> variants;
}
