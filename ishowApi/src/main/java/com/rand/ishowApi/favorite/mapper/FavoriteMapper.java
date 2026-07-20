package com.rand.ishowApi.favorite.mapper;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.favorite.api.response.FavoriteItemResponse;
import com.rand.ishowApi.favorite.domain.entity.Favorite;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FavoriteMapper {
    private final Map<ContentType, FavoriteContentAdapter<?, ?>> adapterMap;

    public FavoriteMapper(List<FavoriteContentAdapter<?, ?>> adapters) {
        this.adapterMap = adapters.stream().collect(Collectors.toMap(
                FavoriteContentAdapter::contentType,
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

    public FavoriteItemResponse<?> map(Favorite favorite) {
        if (favorite == null) {
            return null;
        }

        Object content = findContent(favorite.getContentType(), favorite.getContentId()).orElse(null);
        return map(favorite, content);
    }

    public <T, D> FavoriteItemResponse<D> map(Favorite favorite, T content) {
        if (favorite == null) {
            return null;
        }

        FavoriteItemResponse<D> response = new FavoriteItemResponse<>();
        response.setFavoriteId(favorite.getId());
        response.setContentId(favorite.getContentId());
        response.setContentType(favorite.getContentType());
        response.setContent(content == null ? null : toResponse(favorite.getContentType(), content, DeviceContext.get().getLanguage().toString().toLowerCase()));
        return response;
    }

    public List<FavoriteItemResponse<?>> map(List<Favorite> favorites) {
        if (favorites == null || favorites.isEmpty()) {
            return Collections.emptyList();
        }

        Map<ContentType, Map<String, Object>> contentByType = new EnumMap<>(ContentType.class);

        favorites.stream()
                .map(Favorite::getContentType)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(contentType -> {
                    List<String> contentIds = favorites.stream()
                            .filter(favorite -> contentType.equals(favorite.getContentType()))
                            .map(Favorite::getContentId)
                            .filter(Objects::nonNull)
                            .distinct()
                            .toList();
                    contentByType.put(contentType, findContents(contentType, contentIds));
                });

        List<FavoriteItemResponse<?>> responses = new ArrayList<>(favorites.size());
        for (Favorite favorite : favorites) {
            if (favorite == null) {
                continue;
            }

            Map<String, Object> contentMap = contentByType.getOrDefault(favorite.getContentType(), Collections.emptyMap());
            Object content = contentMap.get(favorite.getContentId());
            if (content == null) {
                continue;
            }
            responses.add(map(favorite, content, DeviceContext.get().getLanguage().toString().toLowerCase()));
        }
        return responses;
    }

    private <T, D> FavoriteItemResponse<D> map(Favorite favorite, T content, String lang) {
        FavoriteItemResponse<D> response = new FavoriteItemResponse<>();
        response.setFavoriteId(favorite.getId());
        response.setContentId(favorite.getContentId());
        response.setContentType(favorite.getContentType());
        response.setContent(content == null ? null : toResponse(favorite.getContentType(), content, lang));
        return response;
    }


    @SuppressWarnings("unchecked")
    private <T, D> D toResponse(ContentType contentType, T content, String lang) {
        return ((FavoriteContentAdapter<T, D>) adapter(contentType)).toResponse(content, lang);
    }

    private FavoriteContentAdapter<?, ?> adapter(ContentType contentType) {
        FavoriteContentAdapter<?, ?> adapter = adapterMap.get(contentType);
        if (adapter == null) {
            throw new CustomException(ApiResponseCode.ERROR_400, null);
        }
        return adapter;
    }


}


