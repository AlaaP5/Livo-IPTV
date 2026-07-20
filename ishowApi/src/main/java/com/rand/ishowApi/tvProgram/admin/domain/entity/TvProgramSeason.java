package com.rand.ishowApi.tvProgram.admin.domain.entity;

import com.rand.ishowApi.audit.MongoBaseEntity;
import com.rand.ishowApi.metadata.Trailer;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TvProgramSeason extends MongoBaseEntity {
    @Id
    private String id;

    @Indexed
    private  String tvProgramId;

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
    private Integer releaseYear;

    @Builder.Default
    private Integer episodeCount = 0;
}
