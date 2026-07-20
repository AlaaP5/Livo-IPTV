package com.rand.ishowApi.metadata;

public enum ContentLanguage {
    Arabic("Arabic", "العربية"),
    English("English", "الإنجليزية"),
    Spanish("Spanish", "الإسبانية"),
    Russian("Russian", "الروسية"),
    Korean("Korean", "الكورية");

    private final String nameEn;
    private final String nameAr;

    ContentLanguage(String nameEn, String nameAr) {
        this.nameEn = nameEn;
        this.nameAr = nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }
}