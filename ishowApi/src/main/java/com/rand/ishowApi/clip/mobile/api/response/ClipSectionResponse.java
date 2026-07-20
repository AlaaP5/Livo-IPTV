package com.rand.ishowApi.clip.mobile.api.response;


import com.rand.ishowApi.metadata.Badge;
import lombok.Data;

import java.util.List;

@Data
public class ClipSectionResponse {
    private String clipId;
    private String title;
    private Badge badge;
    private String poster;
    private String publishDate;
    private String duration;
    private List<String> tags;

}
