package com.rand.ishowApi.channel.admin.domain.entity;

import com.rand.ishowApi.audit.MongoBaseEntity;
import com.rand.ishowApi.metadata.Badge;
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
public class Channel extends MongoBaseEntity {
    @Id
    private String id;
    @TextIndexed
    private String titleEn;
    @TextIndexed
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private String streamUrl;
    private Badge badge;
    private OriginalUploadMetadata logo;
    private List<TagMeta> tags;
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
}
