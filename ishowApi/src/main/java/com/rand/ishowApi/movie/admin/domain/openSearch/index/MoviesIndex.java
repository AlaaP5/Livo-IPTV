package com.rand.ishowApi.movie.admin.domain.openSearch.index;


import com.rand.ishowApi.shared.settings.MobilePage;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum MoviesIndex {

    MOVIES_SECTION("movies_section"),
    MOVIES_SEARCH("movies_search"),
    MOVIES_HOME("movies_home"),
    MOVIES_KIDS("movies_kids"),
    MOVIES_SPORT("movies_sport");

    private final String indexName;

    MoviesIndex(String indexName) {
        this.indexName = indexName;
    }


    public static List<MoviesIndex> getSectionIndexes() {
        return Arrays.stream(values())
                .filter(index -> index != MOVIES_SEARCH)
                .toList();
    }

    public static String getIndexNameByPage(MobilePage mobilePage) {

        return switch (mobilePage) {
            case HOME -> MOVIES_HOME.getIndexName();
            case KIDS -> MOVIES_KIDS.getIndexName();
            case MOVIES -> MOVIES_SECTION.getIndexName();
            case SPORT -> MOVIES_SPORT.getIndexName();
            default -> "";
        };
    }

}