package com.rand.ishowApi.tvProgram.admin.api.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateTvProgramSeasonAdminRequest {
    private String tvProgramId;
    private String seasonId;
    private Integer seasonNumber;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private String active;
    private Integer releaseYear;
    private MultipartFile poster;
    private MultipartFile trailer;
}

