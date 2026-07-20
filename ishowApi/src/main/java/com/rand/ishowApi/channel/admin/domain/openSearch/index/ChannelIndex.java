package com.rand.ishowApi.channel.admin.domain.openSearch.index;


import com.rand.ishowApi.shared.settings.MobilePage;
import lombok.Getter;
import java.util.Arrays;
import java.util.List;

@Getter
public enum ChannelIndex {

    CHANNEL_SECTION("channel_section"),
    CHANNEL_SEARCH("channel_search"),
    CHANNEL_HOME("channel_home"),
    CHANNEL_KIDS("channel_kids"),
    CHANNEL_SPORT("channel_sport");

    private final String indexName;

    ChannelIndex(String indexName) {
        this.indexName = indexName;
    }

    public static List<ChannelIndex> getSectionIndexes() {
        return Arrays.stream(values())
                .filter(index -> index != CHANNEL_SEARCH)
                .toList();
    }

    public static String getIndexNameByPage(MobilePage mobilePage) {

        return switch (mobilePage) {
            case HOME -> CHANNEL_HOME.getIndexName();
            case KIDS -> CHANNEL_KIDS.getIndexName();
            case CHANNELS -> CHANNEL_SECTION.getIndexName();
            case SPORT -> CHANNEL_SPORT.getIndexName();
            default -> "";
        };
    }
}
