package com.rand.ishowApi.historyWatch.api.response;

import com.rand.ishowApi.shared.settings.ContentType;
import lombok.Data;

@Data
public class HistoryWatchItemResponse<T> {
    private String historyWatchId;
    private String contentId;
    private ContentType contentType;
    private T content;
}

