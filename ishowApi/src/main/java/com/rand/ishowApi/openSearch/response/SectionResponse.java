package com.rand.ishowApi.openSearch.response;

import com.rand.ishowApi.shared.settings.ContentType;
import lombok.Data;

import java.util.List;

@Data
public class SectionResponse<T> {
    private ContentType sectionType;
    private Long sectionId;
    private String sectionName;
    private List<T> content;
}
