package com.rand.ishowApi.series.admin.domain.entity;

import com.rand.ishowApi.audit.MongoBaseEntity;
import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import com.rand.ishowApi.metadata.TranscodeStatus;
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
public class SeriesEpisode extends MongoBaseEntity {
    @Id
    private String id;
    @Indexed
    private  String seriesId;
    @Indexed
    private  String seasonId;
    @Indexed
    private Integer episodeNumber;
    @TextIndexed
    private String titleEn;
    @TextIndexed
    private String titleAr;
    private OriginalUploadMetadata poster;
    @Indexed
    private Boolean active;
    @Indexed
    private Boolean isPublish;
    @Indexed
    private TranscodeStatus transcodeStatus;
    private Double rate;
    private AccessType accessType;
    private Badge badge;
    private TranscodeMetaData transcodeMetaData;
    private OriginalUploadMetadata originalUploadMetadata;
    private List<OriginalUploadMetadata> subtitles;

    private Integer duration;

}
