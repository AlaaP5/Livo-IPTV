package com.rand.ishowApi.library.api.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rand.ishowApi.historyWatch.api.response.HistoryWatchItemResponse;
import com.rand.ishowApi.watchList.api.response.WatchListItemResponse;
import lombok.Data;

import java.util.List;

@JsonPropertyOrder({
        "watchList",
        "historyWatch"
})
@Data
public class LibraryResponse {
    private List<WatchListItemResponse<?>> watchList;
    private List<HistoryWatchItemResponse<?>> historyWatch;
}

