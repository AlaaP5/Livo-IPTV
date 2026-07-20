package com.rand.ishowApi.clip.mobile.api.response;


import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import lombok.Data;

import java.util.List;

@Data
public class ClipsDetailsResponse {
    private String clipsId;
    private String title;
    private String description;
    private String poster;
    private List<LookupResponse> tags;
    private Badge badge;
    private String duration;
    private String publishDate;
    private String streamUrl;
    private AccessType accessType;
    private List<ClipsDetailsResponse> relatedClips;
    private Boolean isWatchList;
    private Boolean isLike;
}
