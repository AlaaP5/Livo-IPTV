package com.rand.ishowApi.favorite.mapper;

import com.rand.ishowApi.shared.settings.ContentType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface FavoriteContentAdapter<T, D> {
    ContentType contentType();

    String notFoundCode();

    Optional<T> findById(String contentId);

    Map<String, T> findAllByIds(Collection<String> contentIds);

    D toResponse(T content, String lang);
}


