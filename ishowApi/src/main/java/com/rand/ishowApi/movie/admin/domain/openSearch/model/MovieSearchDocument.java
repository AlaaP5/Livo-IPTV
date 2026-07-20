package com.rand.ishowApi.movie.admin.domain.openSearch.model;

import com.rand.ishowApi.metadata.*;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import lombok.Data;

import java.util.List;


@Data
public class MovieSearchDocument {

    private String id;
    private String titleEn;
    private String titleAr;
    private String descriptionEn;
    private String descriptionAr;
    private OriginalUploadMetadata poster;
    private Badge badge;
    private List<TagMeta> tags;
    private AccessType accessType;
    private List<ActorMeta> actors;
    private Integer releaseYear;

    private Integer duration; //Number of seconds

    private TranscodeMetaData transcodeMetaData;
    private OriginalUploadMetadata originalUploadMetadata;
    private List<OriginalUploadMetadata> subtitles;
    private List<ContentLanguage> subtitleLanguages;
    private List<ContentLanguage> audioLanguages;
    private Trailer trailer;
    private TranscodeStatus transcodeStatus;
    private Boolean isPublish;
    private Boolean hasRight;
    private ContentLanguage language;
    private Boolean active;
    private Boolean isKids;
    private Boolean isSports;
    private Double rating;
}
