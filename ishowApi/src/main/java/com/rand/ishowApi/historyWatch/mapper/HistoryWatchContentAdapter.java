package com.rand.ishowApi.historyWatch.mapper;

import com.rand.ishowApi.shared.settings.ContentType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface HistoryWatchContentAdapter<T, D> {
    ContentType contentType();

    String notFoundCode();

    Optional<T> findById(String contentId);

    Map<String, T> findAllByIds(Collection<String> contentIds);

    D toResponse(T content, String lang);
}

