package com.rand.ishowApi.home.mobile.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rand.ishowApi.shared.settings.ContentType;
import lombok.Data;

import java.util.List;

@Data
public class HomeSectionResponse {
    @JsonProperty("section_id")
    private Long sectionId;
    @JsonProperty("section_title")
    private String sectionTitle;
    @JsonProperty("section_type")
    private ContentType sectionType;
    private List<?> contents;
    @JsonIgnore
    private Integer sectionOrder;
}
