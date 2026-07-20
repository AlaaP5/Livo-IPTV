package com.rand.ishowApi.channel.admin.api.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChannelEpgAdminRequest {
    private String id;
    private String channelId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String titleEn;
    private String titleAr;
    private String active;
}

