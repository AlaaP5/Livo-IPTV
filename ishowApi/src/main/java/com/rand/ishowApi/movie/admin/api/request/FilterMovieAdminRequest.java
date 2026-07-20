package com.rand.ishowApi.movie.admin.api.request;


import com.rand.ishowApi.common.PaginationFilter;
import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import com.rand.ishowApi.metadata.TranscodeStatus;
import lombok.Data;

@Data
public class FilterMovieAdminRequest extends PaginationFilter {
    private String name;
    private TranscodeStatus status;
    private Long accountId;
    private String isActive;
    private String isPublish;
    private String isKids;
    private String isSport;
    private AccessType accessType;
    private Badge badge;
    private ContentLanguage language;
    private Long actorId;

}
