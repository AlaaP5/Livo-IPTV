package com.rand.ishowApi.utils;


import lombok.Getter;

@Getter
public class LocalizedText {

    private final String nameEn;
    private final String nameAr;

    public LocalizedText(String en, String nameAr) {
        this.nameEn = en;
        this.nameAr = nameAr;
    }

    public String get(String lang) {
        if (lang != null && lang.toLowerCase().startsWith("ar")) {
            return nameAr;
        }
        return nameEn;
    }
    public static String getName( String en, String ar,String lang) {
        if (lang != null && lang.toLowerCase().startsWith("ar")) {
            return ar;
        }
        return en;
    }


    private boolean matchesName(String name, String en, String ar) {
        if (name == null || name.isBlank()) return true;
        String keyword = name.toLowerCase();
        return en.toLowerCase().contains(keyword) || ar.toLowerCase().contains(keyword);
    }
}

