package com.rand.ishowApi.movie.mobile.mapper;

import com.rand.ishowApi.movie.admin.domain.openSearch.model.MovieDocument;
import com.rand.ishowApi.movie.mobile.api.response.MovieSectionResponse;
import com.rand.ishowApi.openSearch.mapper.SectionContentMapper;
import com.rand.ishowApi.utils.LocalizedText;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.stream.Collectors;


@Component
public class MovieDocMapper implements SectionContentMapper<MovieDocument, MovieSectionResponse> {

    @Override
    public MovieSectionResponse map(MovieDocument doc, String lang) {

        MovieSectionResponse movie = new MovieSectionResponse();

        movie.setMovieId(doc.getMovieId());
        movie.setTitle(LocalizedText.getName(doc.getTitleEn(), doc.getTitleAr(), lang));
        movie.setBadge(doc.getBadge());
        movie.setReleaseYear(doc.getReleaseYear());
        movie.setDuration(doc.getDuration());

        movie.setRating(
                doc.getRating() != null
                        ? BigDecimal.valueOf(doc.getRating())
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue()
                        : 0.0
        );

        // Poster
        if (doc.getPoster() != null) {
            movie.setPoster(doc.getPoster().getGeneratedPath());
        }

        if (doc.getTags() != null) {
            movie.setTags(
                    doc.getTags().stream()
                            .map(tag -> LocalizedText.getName(
                                    tag.getTitleEn(),
                                    tag.getTitleAr(),
                                    lang
                            ))
                            .collect(Collectors.toList())
            );
        } else {
            movie.setTags(Collections.emptyList());
        }


        return movie;
    }
}
