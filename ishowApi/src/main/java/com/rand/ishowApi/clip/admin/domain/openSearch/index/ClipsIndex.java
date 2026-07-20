package com.rand.ishowApi.clip.admin.domain.openSearch.index;

import com.rand.ishowApi.shared.settings.MobilePage;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ClipsIndex {
    CLIPS_SECTION("clips_section"),
    CLIPS_SEARCH("clips_search"),
    CLIPS_HOME("clips_home"),
    CLIPS_KIDS("clips_kids"),
    CLIPS_SPORT("clips_sport");

    private final String indexName;

    ClipsIndex(String indexName) {
        this.indexName = indexName;
    }

    public static List<ClipsIndex> getSectionIndexes() {
        return Arrays.stream(values())
                .filter(index -> index != CLIPS_SEARCH)
                .toList();
    }

    public static String getIndexNameByPage(MobilePage mobilePage) {

        return switch (mobilePage) {
            case HOME -> CLIPS_HOME.getIndexName();
            case KIDS -> CLIPS_KIDS.getIndexName();
            case CLIPS -> CLIPS_SECTION.getIndexName();
            case SPORT -> CLIPS_SPORT.getIndexName();
            default -> "";
        };
    }
}

