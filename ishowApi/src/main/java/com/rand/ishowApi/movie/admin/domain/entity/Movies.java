package com.rand.ishowApi.movie.admin.domain.entity;

import com.rand.ishowApi.audit.MongoBaseEntity;
import com.rand.ishowApi.metadata.*;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
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
public class Movies extends MongoBaseEntity {
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
    private AccessType accessType;
    private List<ActorMeta> actors;

    @Indexed
    private Integer releaseYear;

    private Integer duration; //Number of seconds

    private TranscodeMetaData transcodeMetaData;
    private OriginalUploadMetadata originalUploadMetadata;
    private List<OriginalUploadMetadata> subtitles;
    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;
    private Trailer trailer;

    @Indexed
    private TranscodeStatus transcodeStatus;

    @Indexed
    private Boolean isPublish;

    @Indexed
    private Boolean hasRight;

    @Indexed
    private ContentLanguage language;

    @Indexed
    private Boolean active;

    @Indexed
    private Boolean isKids;

    @Indexed
    private Boolean isSports;

    private Double rating;
}
