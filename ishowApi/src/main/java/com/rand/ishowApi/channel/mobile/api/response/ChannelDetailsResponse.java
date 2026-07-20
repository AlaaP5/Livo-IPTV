package com.rand.ishowApi.channel.mobile.api.response;

import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.metadata.Badge;
import lombok.Data;

import java.util.List;

@Data
public class ChannelDetailsResponse {

    private String channelId;
    private String title;
    private String description;
    private String logo;
    private String streamUrl;
    private Badge badge;
    private List<LookupResponse> tags;
    private List<ChannelEpgResponse> epg;

    private Boolean isWatchList;
    private Boolean isLike;
}



