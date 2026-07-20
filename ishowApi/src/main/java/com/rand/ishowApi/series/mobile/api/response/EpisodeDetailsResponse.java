package com.rand.ishowApi.series.mobile.api.response;


import com.rand.ishowApi.metadata.AccessType;
import com.rand.ishowApi.metadata.Badge;
import lombok.Data;

@Data
public class EpisodeDetailsResponse {
  private String episodeId;
  private Integer episodeNumber;
  private String title;
  private String poster;
  private Double rating;
  private AccessType accessType;
  private Badge badge;
  private String streamUrl;
  private String duration;
  private java.util.List<String> subtitles;
}
