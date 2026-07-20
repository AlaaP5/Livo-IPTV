package com.rand.ishowApi.clip.admin.domain.entity;

import com.rand.ishowApi.audit.MongoBaseEntity;
import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Document
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Clip extends MongoBaseEntity {
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
    private Integer duration;
    private TranscodeMetaData transcodeMetaData;
    private OriginalUploadMetadata originalUploadMetadata;
    @Indexed
    private TranscodeStatus transcodeStatus;
    @Indexed
    private Boolean isPublish;
    @Indexed
    private Boolean hasRight;
    @Indexed
    private Boolean active;
    @Indexed
    private Boolean isKids;
    @Indexed
    private Boolean isSports;

    protected String publishDate;
}
