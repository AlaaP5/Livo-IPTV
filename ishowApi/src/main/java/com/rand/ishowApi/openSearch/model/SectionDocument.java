package com.rand.ishowApi.openSearch.model;

import lombok.Data;

@Data
public class SectionDocument {
    private Long  sectionId;
    private String  sectionTitleAr;
    private String  sectionTitleEn;
    private Integer sectionOrder;
    private boolean sectionActive;
    private boolean sectionPublish;
}
