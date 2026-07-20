package com.rand.ishowApi.tvProgram.admin.api.request;

import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AddTvProgramEpisodeAdminRequest {
    private String tvProgramId;
    private String seasonId;
    private Integer episodeNumber;
    private String titleEn;
    private String titleAr;
    private String active;
    private Integer duration;
    private AccessType accessType;
    private Badge badge;
    private MultipartFile poster;
    private MultipartFile file;
    private List<MultipartFile> subtitles;
}

