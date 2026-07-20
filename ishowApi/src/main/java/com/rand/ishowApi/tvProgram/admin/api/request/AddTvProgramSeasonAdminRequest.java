package com.rand.ishowApi.tvProgram.admin.api.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddTvProgramSeasonAdminRequest {
    private String tvProgramId;
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

