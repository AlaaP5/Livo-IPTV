package com.rand.ishowApi.series.admin.api.request;

import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AddSeriesEpisodeAdminRequest {
    private String seriesId;
    private String seasonId;
    private Integer episodeNumber;
    private String titleEn;
    private String titleAr;
    private String active;
    private Double rate;
    private Integer duration;
    private AccessType accessType;
    private Badge badge;
    private MultipartFile poster;
    private MultipartFile file;
    private List<MultipartFile> subtitles;
}

