package com.rand.ishowApi.channel.admin.api.request;

import com.rand.ishowApi.metadata.Badge;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ChannelAdminRequest {
    private String id;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private Badge badge;
    private List<Long> tags;
    private String hasRight;
    private String isKids;
    private String active;
    private String isSports;
    private MultipartFile logo;
}
