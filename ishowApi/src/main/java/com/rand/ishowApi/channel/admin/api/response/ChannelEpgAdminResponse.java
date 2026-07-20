package com.rand.ishowApi.channel.admin.api.response;

import com.rand.ishowApi.lookup.api.response.MongoLookupResponse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChannelEpgAdminResponse {
    private String id;
    private MongoLookupResponse channel;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String titleEn;
    private String titleAr;
    private Boolean active;
}

