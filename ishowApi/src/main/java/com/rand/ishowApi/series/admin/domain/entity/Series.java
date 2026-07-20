package com.rand.ishowApi.series.admin.domain.entity;

import com.rand.ishowApi.audit.MongoBaseEntity;
import com.rand.ishowApi.metadata.ActorMeta;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.ContentLanguage;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Document
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Series extends MongoBaseEntity {
    @Id
    private String id;

    @TextIndexed
    private String titleEn;

    @TextIndexed
    private String titleAr;

    private String descriptionEn;
    private String descriptionAr;
    private OriginalUploadMetadata poster;
    private Badge badge;
    private List<TagMeta> tags;
    private List<ActorMeta> actors;

    @Indexed
    private ContentLanguage language;

    @Indexed
    private Boolean active;

    @Indexed
    private Boolean isPublish;

    @Indexed
    private Boolean hasRight;

    @Indexed
    private Boolean isKids;

    @Indexed
    private Boolean isSports;

    private Double rating;

    private Integer releaseYear;

    @Builder.Default
    private Integer seasonCount = 0;

    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;


}
