package com.rand.ishowApi.library.mapper;

import com.rand.ishowApi.historyWatch.api.response.HistoryWatchItemResponse;
import com.rand.ishowApi.library.api.response.LibraryResponse;
import com.rand.ishowApi.watchList.api.response.WatchListItemResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LibraryMapper {
    public LibraryResponse map(List<WatchListItemResponse<?>> watchList,
                               List<HistoryWatchItemResponse<?>> historyWatch) {
        LibraryResponse response = new LibraryResponse();
        response.setWatchList(watchList);
        response.setHistoryWatch(historyWatch);
        return response;
    }
}

