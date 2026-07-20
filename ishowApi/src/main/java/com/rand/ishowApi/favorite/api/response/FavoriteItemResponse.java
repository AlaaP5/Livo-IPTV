package com.rand.ishowApi.favorite.api.response;

import com.rand.ishowApi.shared.settings.ContentType;
import lombok.Data;

@Data
public class FavoriteItemResponse<T> {
    private String favoriteId;
    private String contentId;
    private ContentType contentType;
    private T content;
}


