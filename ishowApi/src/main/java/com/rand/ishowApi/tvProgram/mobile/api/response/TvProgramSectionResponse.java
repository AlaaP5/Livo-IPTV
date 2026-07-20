package com.rand.ishowApi.tvProgram.mobile.api.response;


import com.rand.ishowApi.metadata.Badge;
import lombok.Data;

import java.util.List;

@Data
public class TvProgramSectionResponse {
    private String tvProgramId;
    private String title;
    private Badge badge;
    private String poster;
    private List<String> tags;
    private Integer releaseYear;
    private Integer seasonCount;
}
