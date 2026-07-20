package com.rand.ishowApi.movie.admin.api.response;

import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class MovieAdminSectionResponse {
    private String movieId;
    private String titleEn;
    private String titleAr;
    private OriginalUploadMetadata poster;
    private Timestamp createDate;
    private Boolean isTop;
    private Boolean isKids;
    private Boolean isSports;
    private Boolean isPublish;
    private Long sectionId;
    private String sectionTitleAr;
    private String sectionTitleEn;
    private Integer sectionOrder;
    private boolean sectionActive;
    private boolean sectionPublish;

}