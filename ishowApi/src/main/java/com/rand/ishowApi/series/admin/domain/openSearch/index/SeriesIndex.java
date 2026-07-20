package com.rand.ishowApi.series.admin.domain.openSearch.index;

import com.rand.ishowApi.shared.settings.MobilePage;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum SeriesIndex {
    SERIES_SECTION("series_section"),
    SERIES_SEARCH("series_search"),
    SERIES_HOME("series_home"),
    SERIES_KIDS("series_kids"),
    SERIES_SPORT("series_sport");

    private final String indexName;

    SeriesIndex(String indexName) {
        this.indexName = indexName;
    }

    public static List<SeriesIndex> getSectionIndexes() {
        return Arrays.stream(values())
                .filter(index -> index != SERIES_SEARCH)
                .toList();
    }
    public static String getIndexNameByPage(MobilePage mobilePage) {

        return switch (mobilePage) {
            case HOME -> SERIES_HOME.getIndexName();
            case KIDS -> SERIES_KIDS.getIndexName();
            case SERIES -> SERIES_SECTION.getIndexName();
            case SPORT -> SERIES_SPORT.getIndexName();
            default -> "";
        };
    }

}
