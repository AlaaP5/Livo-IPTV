package com.rand.ishowApi.favorite.api.request;

import com.rand.ishowApi.shared.settings.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FavoriteRequest {
    @NotBlank
    private String contentId;

    @NotNull
    private ContentType contentType;
}


