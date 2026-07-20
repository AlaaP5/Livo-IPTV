package com.rand.ishowApi.tvProgram.mobile.api.response;


import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import lombok.Data;

import java.util.List;

@Data
public class TvProgramEpisodeDetailsResponse {
    private String episodeId;
    private Integer episodeNumber;
    private String title;
    private String poster;
    private AccessType accessType;
    private Badge badge;
    private String streamUrl;
    private String duration;
    private List<String> subtitles;
}
