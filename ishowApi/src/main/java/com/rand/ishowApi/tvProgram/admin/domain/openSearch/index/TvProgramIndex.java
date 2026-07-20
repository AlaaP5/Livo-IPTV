package com.rand.ishowApi.tvProgram.admin.domain.openSearch.index;

import com.rand.ishowApi.shared.settings.MobilePage;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum TvProgramIndex {
    TV_PROGRAM_SECTION("tv_program_section"),
    TV_PROGRAM_SEARCH("tv_program_search"),
    TV_PROGRAM_HOME("tv_program_home"),
    TV_PROGRAM_KIDS("tv_program_kids"),
    TV_PROGRAM_SPORT("tv_program_sport");

    private final String indexName;

    TvProgramIndex(String indexName) {
        this.indexName = indexName;
    }

    public static List<TvProgramIndex> getSectionIndexes() {
        return Arrays.stream(values())
                .filter(index -> index != TV_PROGRAM_SEARCH)
                .toList();
    }

    public static String getIndexNameByPage(MobilePage mobilePage) {

        return switch (mobilePage) {
            case HOME -> TV_PROGRAM_HOME.getIndexName();
            case KIDS -> TV_PROGRAM_KIDS.getIndexName();
            case TV_PROGRAMS -> TV_PROGRAM_SECTION.getIndexName();
            case SPORT -> TV_PROGRAM_SPORT.getIndexName();
            default -> "";
        };
    }
}
