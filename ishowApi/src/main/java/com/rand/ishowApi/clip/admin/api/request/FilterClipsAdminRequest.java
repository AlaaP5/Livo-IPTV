package com.rand.ishowApi.clip.admin.api.request;
import com.rand.ishowApi.common.PaginationFilter;
import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.TranscodeStatus;
import lombok.Data;
@Data
public class FilterClipsAdminRequest extends PaginationFilter {
    private String name;
    private TranscodeStatus status;
    private Long accountId;
    private String isActive;
    private String isPublish;
    private String isKids;
    private String isSport;
    private String hasRight;
    private AccessType accessType;
    private Badge badge;
}
