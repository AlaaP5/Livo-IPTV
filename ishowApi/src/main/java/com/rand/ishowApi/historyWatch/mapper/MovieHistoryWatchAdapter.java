package com.rand.ishowApi.historyWatch.mapper;

import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.movie.admin.domain.repository.MoviesRepository;
import com.rand.ishowApi.movie.mobile.api.response.MovieSectionResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.utils.LocalizedText;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovieHistoryWatchAdapter implements HistoryWatchContentAdapter<Movies, MovieSectionResponse> {
    private final MoviesRepository moviesRepository;

    @Override
    public ContentType contentType() {
        return ContentType.MOVIES;
    }

    @Override
    public String notFoundCode() {
        return ApiResponseCode.MOVIE_NOT_FOUND;
    }

    @Override
    public Optional<Movies> findById(String contentId) {
        if (contentId == null) {
            return Optional.empty();
        }
        return moviesRepository.findById(contentId);
    }

    @Override
    public Map<String, Movies> findAllByIds(Collection<String> contentIds) {
        if (contentIds == null || contentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Movies> movies = new ArrayList<>(moviesRepository.findAllById(contentIds));
        return movies.stream().collect(Collectors.toMap(
                Movies::getId,
                Function.identity(),
                (left, right) -> left,
                LinkedHashMap::new
        ));
    }

    @Override
    public MovieSectionResponse toResponse(Movies movie, String lang) {
        MovieSectionResponse response = new MovieSectionResponse();
        response.setMovieId(movie.getId());
        response.setTitle(LocalizedText.getName(movie.getTitleEn(), movie.getTitleAr(), lang));
        response.setBadge(movie.getBadge());
        response.setRating(movie.getRating() == null ? 0.0 : movie.getRating());
        response.setReleaseYear(movie.getReleaseYear());
        response.setDuration(movie.getDuration());
        if (movie.getPoster() != null) {
            response.setPoster(movie.getPoster().getGeneratedPath());
        }
        response.setTags(movie.getTags() == null ? Collections.emptyList() : movie.getTags().stream()
                .map(tag -> mapTag(tag, lang))
                .collect(Collectors.toList()));
        return response;
    }

    private String mapTag(TagMeta tag, String lang) {
        return tag == null ? null : LocalizedText.getName(tag.getTitleEn(), tag.getTitleAr(), lang);
    }
}

