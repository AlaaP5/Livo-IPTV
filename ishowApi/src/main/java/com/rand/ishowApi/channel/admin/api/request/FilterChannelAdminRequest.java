package com.rand.ishowApi.channel.admin.api.request;

import com.rand.ishowApi.common.PaginationFilter;
import com.rand.ishowApi.metadata.Badge;
import lombok.Data;

@Data
public class FilterChannelAdminRequest extends PaginationFilter {

    private String name;
    private Long accountId;
    private String isActive;
    private String isPublish;
    private String isKids;
    private String isSport;
    private Badge badge;
    private Long tagId;
}
