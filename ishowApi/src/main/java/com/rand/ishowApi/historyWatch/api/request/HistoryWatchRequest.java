package com.rand.ishowApi.historyWatch.api.request;

import com.rand.ishowApi.shared.settings.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HistoryWatchRequest {
    @NotBlank
    private String contentId;

    @NotNull
    private ContentType contentType;
}

