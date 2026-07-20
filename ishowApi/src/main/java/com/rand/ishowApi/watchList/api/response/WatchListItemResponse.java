package com.rand.ishowApi.watchList.api.response;

import com.rand.ishowApi.shared.settings.ContentType;
import lombok.Data;

@Data
public class WatchListItemResponse<T> {
    private String watchListId;
    private String contentId;
    private ContentType contentType;
    private T content;
}

