package com.rand.ishowApi.tvProgram.admin.domain.openSearch.model;


import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.openSearch.model.SectionDocument;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.List;

@Document
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TvProgramDocument extends SectionDocument {
    @Id
    private String tvProgramId;
    private String  titleEn;
    private String  titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private OriginalUploadMetadata poster;
    private Badge badge;
    private List<TagMeta> tags;
    private ContentLanguage language;
    private Boolean active;
    private Boolean isPublish;
    private Boolean hasRight;
    private Boolean isKids;
    private Boolean isSports;
    private Integer releaseYear;
    private Integer seasonCount;
    private java.util.List<ContentLanguage> subtitleLanguages;
    private java.util.List<ContentLanguage> audioLanguages;
    private Boolean isTop;
    private Timestamp createDate;
}
