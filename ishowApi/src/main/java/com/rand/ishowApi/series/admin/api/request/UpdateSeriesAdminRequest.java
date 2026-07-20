package com.rand.ishowApi.series.admin.api.request;

import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UpdateSeriesAdminRequest {
    private String seriesId;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private Badge badge;
    private List<Long> tags;
    private ContentLanguage language;
    private String hasRight;
    private String isKids;
    private String active;
    private String isSports;
    private Double rate;
    private Integer releaseYear;
    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;
    private MultipartFile poster;
    private List<Long> actors;

}

