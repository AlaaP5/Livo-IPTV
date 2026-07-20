package com.rand.ishowApi.series.admin.domain.entity;

import com.rand.ishowApi.audit.MongoBaseEntity;
import com.rand.ishowApi.metadata.ActorMeta;
import com.rand.ishowApi.metadata.Trailer;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Builder
@Document
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SeriesSeason extends MongoBaseEntity {
    @Id
    private String id;
    @Indexed
    private  String seriesId;
    @Indexed
    private Integer seasonNumber;
    @TextIndexed
    private String titleEn;
    @TextIndexed
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private OriginalUploadMetadata poster;
    @Indexed
    private Boolean active;
    private Trailer trailer;
    private List<ActorMeta> actors;
    private Integer releaseYear;

    @Builder.Default
    private Integer episodeCount = 0;

    private Double rating;


}
