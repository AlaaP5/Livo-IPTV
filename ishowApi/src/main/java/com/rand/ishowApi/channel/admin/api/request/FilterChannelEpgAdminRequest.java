package com.rand.ishowApi.channel.admin.api.request;

import com.rand.ishowApi.common.PaginationFilter;
import lombok.Data;

@Data
public class FilterChannelEpgAdminRequest extends PaginationFilter {
    private String channelId;
    private String startDate; // ISO date-time string expected
    private String endDate;   // ISO date-time string expected
    private String active;
    private String title;
}

