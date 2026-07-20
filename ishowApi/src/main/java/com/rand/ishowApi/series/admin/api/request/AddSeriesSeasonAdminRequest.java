package com.rand.ishowApi.series.admin.api.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AddSeriesSeasonAdminRequest {
    private String seriesId;
    private Integer seasonNumber;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private String active;
    private Integer releaseYear;
    private List<Long> actors;
    private MultipartFile poster;
    private MultipartFile trailer;
    private Double rating;
}

