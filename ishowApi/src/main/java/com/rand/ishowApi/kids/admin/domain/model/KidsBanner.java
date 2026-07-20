package com.rand.ishowApi.kids.admin.domain.model;

import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.shared.settings.ContentType;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class KidsBanner {
    private String contentId;
    private String contentArabicTitle;
    private String contentEnglishTitle;
    private String poster;
    private ContentType contentType;
    private List<TagMeta> tagMeta;
    private Timestamp createAt;
    private Boolean isKids;
}

