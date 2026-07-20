package com.rand.ishowApi.movie.admin.api.request;


import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class MovieAdminRequest {
    private String movieId;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private Badge badge;
    private List<Long> tags;
    private AccessType accessType;
    private List<Long> actors;
    private Integer releaseYear;
    private ContentLanguage language;
    private String hasRight;
    private String isKids;
    private String active;
    private String isSports;
    private Double rating;
    private List<MultipartFile> subtitles;
    private MultipartFile trailer;
    private MultipartFile file;
    private MultipartFile poster;
    private Integer duration;

    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;


}
