package com.rand.ishowApi.watchList.mapper;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.watchList.api.response.WatchListItemResponse;
import com.rand.ishowApi.watchList.domain.entity.WatchList;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class WatchListMapper {
    private final Map<ContentType, WatchListContentAdapter<?, ?>> adapterMap;

    public WatchListMapper(List<WatchListContentAdapter<?, ?>> adapters) {
        this.adapterMap = adapters.stream().collect(Collectors.toMap(
                WatchListContentAdapter::contentType,
                Function.identity(),
                (left, right) -> left,
                () -> new EnumMap<>(ContentType.class)
        ));
    }

    public String notFoundCode(ContentType contentType) {
        return adapter(contentType).notFoundCode();
    }


    public <T> Optional<T> findContent(ContentType contentType, String contentId) {
        return adapter(contentType).findById(contentId).map(content -> (T) content);
    }


    public <T> Map<String, T> findContents(ContentType contentType, Collection<String> contentIds) {
        return adapter(contentType).findAllByIds(contentIds).entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> (T) entry.getValue(),
                (left, right) -> left,
                java.util.LinkedHashMap::new
        ));
    }

    public WatchListItemResponse<?> map(WatchList watchList) {
        if (watchList == null) {
            return null;
        }

        Object content = findContent(watchList.getContentType(), watchList.getContentId()).orElse(null);
        return map(watchList, content);
    }

    public <T, D> WatchListItemResponse<D> map(WatchList watchList, T content) {
        if (watchList == null) {
            return null;
        }

        WatchListItemResponse<D> response = new WatchListItemResponse<>();
        response.setWatchListId(watchList.getId());
        response.setContentId(watchList.getContentId());
        response.setContentType(watchList.getContentType());
        response.setContent(content == null ? null : toResponse(watchList.getContentType(), content, DeviceContext.get().getLanguage().toString().toLowerCase()));
        return response;
    }

    public List<WatchListItemResponse<?>> map(List<WatchList> watchLists) {
        if (watchLists == null || watchLists.isEmpty()) {
            return Collections.emptyList();
        }

        Map<ContentType, Map<String, Object>> contentByType = new EnumMap<>(ContentType.class);

        watchLists.stream()
                .map(WatchList::getContentType)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(contentType -> {
                    List<String> contentIds = watchLists.stream()
                            .filter(watchList -> contentType.equals(watchList.getContentType()))
                            .map(WatchList::getContentId)
                            .filter(Objects::nonNull)
                            .distinct()
                            .toList();
                    contentByType.put(contentType, findContents(contentType, contentIds));
                });

        List<WatchListItemResponse<?>> responses = new ArrayList<>(watchLists.size());
        for (WatchList watchList : watchLists) {
            if (watchList == null) {
                continue;
            }

            Map<String, Object> contentMap = contentByType.getOrDefault(watchList.getContentType(), Collections.emptyMap());
            Object content = contentMap.get(watchList.getContentId());
            if (content == null) {
                continue;
            }
            responses.add(map(watchList, content, DeviceContext.get().getLanguage().toString().toLowerCase()));
        }
        return responses;
    }

    private <T, D> WatchListItemResponse<D> map(WatchList watchList, T content, String lang) {
        WatchListItemResponse<D> response = new WatchListItemResponse<>();
        response.setWatchListId(watchList.getId());
        response.setContentId(watchList.getContentId());
        response.setContentType(watchList.getContentType());
        response.setContent(content == null ? null : toResponse(watchList.getContentType(), content, lang));
        return response;
    }


    @SuppressWarnings("unchecked")
    private <T, D> D toResponse(ContentType contentType, T content, String lang) {
        return ((WatchListContentAdapter<T, D>) adapter(contentType)).toResponse(content, lang);
    }

    private WatchListContentAdapter<?, ?> adapter(ContentType contentType) {
        WatchListContentAdapter<?, ?> adapter = adapterMap.get(contentType);
        if (adapter == null) {
            throw new CustomException(ApiResponseCode.ERROR_400, null);
        }
        return adapter;
    }


}


