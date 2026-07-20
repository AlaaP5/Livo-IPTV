package com.rand.ishowApi.clip.admin.api.request;

import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ClipAdminRequest {
    private String clipId;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private Badge badge;
    private List<Long> tags;
    private AccessType accessType;
    private Integer duration;
    private String hasRight;
    private String active;
    private String isKids;
    private String isSports;
    private MultipartFile file;
    private MultipartFile poster;
}

