package com.rand.ishowApi.historyWatch.mapper;

import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.repository.SeriesRepository;
import com.rand.ishowApi.series.mobile.api.response.SeriesSectionResponse;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.utils.LocalizedText;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SeriesHistoryWatchAdapter implements HistoryWatchContentAdapter<Series, SeriesSectionResponse> {
    private final SeriesRepository seriesRepository;

    @Override
    public ContentType contentType() {
        return ContentType.SERIES;
    }

    @Override
    public String notFoundCode() {
        return ApiResponseCode.SERIES_NOT_FOUND;
    }

    @Override
    public Optional<Series> findById(String contentId) {
        if (contentId == null) {
            return Optional.empty();
        }
        return seriesRepository.findById(contentId);
    }

    @Override
    public Map<String, Series> findAllByIds(Collection<String> contentIds) {
        if (contentIds == null || contentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Series> seriesList = new ArrayList<>(seriesRepository.findAllById(contentIds));
        return seriesList.stream().collect(Collectors.toMap(
                Series::getId,
                Function.identity(),
                (left, right) -> left,
                LinkedHashMap::new
        ));
    }

    @Override
    public SeriesSectionResponse toResponse(Series series, String lang) {
        SeriesSectionResponse response = new SeriesSectionResponse();
        response.setSeriesId(series.getId());
        response.setTitle(LocalizedText.getName(series.getTitleEn(), series.getTitleAr(), lang));
        response.setBadge(series.getBadge());
        response.setRating(series.getRating() == null ? 0.0 : series.getRating());
        response.setReleaseYear(series.getReleaseYear());
        response.setSeasonCount(series.getSeasonCount());
        if (series.getPoster() != null) {
            response.setPoster(series.getPoster().getGeneratedPath());
        }
        response.setTags(series.getTags() == null ? Collections.emptyList() : series.getTags().stream()
                .map(tag -> mapTag(tag, lang))
                .toList());
        return response;
    }

    private String mapTag(TagMeta tag, String lang) {
        return tag == null ? null : LocalizedText.getName(tag.getTitleEn(), tag.getTitleAr(), lang);
    }
}

