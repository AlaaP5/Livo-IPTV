package com.rand.ishowApi.library.application.service;

import com.rand.ishowApi.historyWatch.api.response.HistoryWatchItemResponse;
import com.rand.ishowApi.historyWatch.application.service.HistoryWatchService;
import com.rand.ishowApi.library.api.response.LibraryResponse;
import com.rand.ishowApi.library.mapper.LibraryMapper;
import com.rand.ishowApi.watchList.api.response.WatchListItemResponse;
import com.rand.ishowApi.watchList.application.service.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final WatchListService watchListService;
    private final HistoryWatchService historyWatchService;
    private final LibraryMapper libraryMapper;

    public LibraryResponse getMyLibrary(String contentType, int page, int size) {
        Page<WatchListItemResponse<?>> watchList = watchListService.getWatchList(contentType, page, size);
        Page<HistoryWatchItemResponse<?>> historyWatch = historyWatchService.getHistoryWatch(contentType, page, size);

        return libraryMapper.map(watchList.getContent(), historyWatch.getContent());
    }
}

