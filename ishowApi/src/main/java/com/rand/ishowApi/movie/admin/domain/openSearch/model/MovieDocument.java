package com.rand.ishowApi.movie.admin.domain.openSearch.model;


import com.rand.ishowApi.metadata.*;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.openSearch.model.SectionDocument;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class MovieDocument extends SectionDocument {

  private String  movieId;
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
  private TranscodeMetaData transcodeMetaData;
  private OriginalUploadMetadata originalUploadMetadata;
  private List<OriginalUploadMetadata> subtitles;
  private List<ContentLanguage> subtitleLanguages;
  private List<ContentLanguage> audioLanguages;
  private Trailer trailer;
  private Integer duration;
  private TranscodeStatus transcodeStatus;
  private Boolean isPublish;
  private Boolean hasRight;
  private ContentLanguage language;
  private Boolean active;
  private Boolean isKids;
  private Boolean isSports;
  private Double rating;
  private Boolean isTop;
  private Timestamp createDate;

}
