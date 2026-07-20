package com.rand.ishowApi.channel.mobile.api.response;

import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.lookup.api.response.LookupResponse;
import lombok.Data;

import java.util.List;

@Data
public class ChannelSectionResponse {

    private String channelId;
    private String title;
    private String logo;
    private Badge badge;
    private List<String> tags;
    private ChannelEpgResponse currentProgram;

}

