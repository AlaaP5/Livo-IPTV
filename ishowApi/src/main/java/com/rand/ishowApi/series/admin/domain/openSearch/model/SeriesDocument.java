package com.rand.ishowApi.series.admin.domain.openSearch.model;

import com.rand.ishowApi.metadata.ActorMeta;
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
public class SeriesDocument  extends SectionDocument {
    @Id
    private String  seriesId;
    private String  titleEn;
    private String  titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private OriginalUploadMetadata poster;
    private List<TagMeta> tags;
    private List<ActorMeta> actors;
    private Badge badge;
    private Integer releaseYear;
    private Boolean isPublish;
    private Boolean hasRight;
    private ContentLanguage language;
    private Boolean active;
    private Boolean isKids;
    private Boolean isSports;
    private Double rating;
    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;
    private Boolean isTop;
    private Timestamp createDate;
    private Integer seasonCount;
}
