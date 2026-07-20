package com.rand.ishowApi.historyWatch.mapper;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.historyWatch.api.response.HistoryWatchItemResponse;
import com.rand.ishowApi.historyWatch.domain.entity.HistoryWatch;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class HistoryWatchMapper {
    private final Map<ContentType, HistoryWatchContentAdapter<?, ?>> adapterMap;

    public HistoryWatchMapper(List<HistoryWatchContentAdapter<?, ?>> adapters) {
        this.adapterMap = adapters.stream().collect(Collectors.toMap(
                HistoryWatchContentAdapter::contentType,
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

    public HistoryWatchItemResponse<?> map(HistoryWatch historyWatch) {
        if (historyWatch == null) {
            return null;
        }

        Object content = findContent(historyWatch.getContentType(), historyWatch.getContentId()).orElse(null);
        return map(historyWatch, content);
    }

    public <T, D> HistoryWatchItemResponse<D> map(HistoryWatch historyWatch, T content) {
        if (historyWatch == null) {
            return null;
        }

        HistoryWatchItemResponse<D> response = new HistoryWatchItemResponse<>();
        response.setHistoryWatchId(historyWatch.getId());
        response.setContentId(historyWatch.getContentId());
        response.setContentType(historyWatch.getContentType());
        response.setContent(content == null ? null : toResponse(historyWatch.getContentType(), content, DeviceContext.get().getLanguage().toString().toLowerCase()));
        return response;
    }

    public List<HistoryWatchItemResponse<?>> map(List<HistoryWatch> historyWatches) {
        if (historyWatches == null || historyWatches.isEmpty()) {
            return Collections.emptyList();
        }

        Map<ContentType, Map<String, Object>> contentByType = new EnumMap<>(ContentType.class);

        historyWatches.stream()
                .map(HistoryWatch::getContentType)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(contentType -> {
                    List<String> contentIds = historyWatches.stream()
                            .filter(historyWatch -> contentType.equals(historyWatch.getContentType()))
                            .map(HistoryWatch::getContentId)
                            .filter(Objects::nonNull)
                            .distinct()
                            .toList();
                    contentByType.put(contentType, findContents(contentType, contentIds));
                });

        List<HistoryWatchItemResponse<?>> responses = new ArrayList<>(historyWatches.size());
        for (HistoryWatch historyWatch : historyWatches) {
            if (historyWatch == null) {
                continue;
            }

            Map<String, Object> contentMap = contentByType.getOrDefault(historyWatch.getContentType(), Collections.emptyMap());
            Object content = contentMap.get(historyWatch.getContentId());
            if (content == null) {
                continue;
            }
            responses.add(map(historyWatch, content, DeviceContext.get().getLanguage().toString().toLowerCase()));
        }
        return responses;
    }

    private <T, D> HistoryWatchItemResponse<D> map(HistoryWatch historyWatch, T content, String lang) {
        HistoryWatchItemResponse<D> response = new HistoryWatchItemResponse<>();
        response.setHistoryWatchId(historyWatch.getId());
        response.setContentId(historyWatch.getContentId());
        response.setContentType(historyWatch.getContentType());
        response.setContent(content == null ? null : toResponse(historyWatch.getContentType(), content, lang));
        return response;
    }


    @SuppressWarnings("unchecked")
    private <T, D> D toResponse(ContentType contentType, T content, String lang) {
        return ((HistoryWatchContentAdapter<T, D>) adapter(contentType)).toResponse(content, lang);
    }

    private HistoryWatchContentAdapter<?, ?> adapter(ContentType contentType) {
        HistoryWatchContentAdapter<?, ?> adapter = adapterMap.get(contentType);
        if (adapter == null) {
            throw new CustomException(ApiResponseCode.ERROR_400, null);
        }
        return adapter;
    }


}

