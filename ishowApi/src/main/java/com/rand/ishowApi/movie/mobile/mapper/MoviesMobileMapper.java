package com.rand.ishowApi.movie.mobile.mapper;

import com.rand.ishowApi.lookup.api.response.LookupPosterResponse;
import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.movie.mobile.api.response.MovieDetailsResponse;
import com.rand.ishowApi.movie.mobile.api.response.RelatedMovie;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.utils.DurationConverter;
import com.rand.ishowApi.utils.LocalizedText;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MoviesMobileMapper {



    public MovieDetailsResponse toMovieDetails(Movies movie, List<Movies> relatedMovies, boolean isLke,  boolean isWatchList) {
        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();

        MovieDetailsResponse response = new MovieDetailsResponse();

        response.setMovieId(movie.getId());
        response.setTitle( LocalizedText.getName(movie.getTitleEn(),movie.getTitleAr(),lang) );
        response.setDescription(LocalizedText.getName( movie.getDescriptionEn(),movie.getDescriptionAr(),lang));

        if (movie.getPoster() != null) {
            response.setPoster(movie.getPoster().getGeneratedPath());
        }

        if (movie.getTrailer() != null) {
            //response.setTrailerUrl(movie.getTrailer().getTranscodeMetaData().getMasterPlaylist());
            response.setTrailerUrl("https://www.youtube.com/shorts/eOf-mUbPrUw");
        }

        if(movie.getTranscodeMetaData() != null) {
            //response.setStreamUrl(movie.getTranscodeMetaData().getMasterPlaylist());
            response.setStreamUrl("https://www.youtube.com/shorts/eOf-mUbPrUw");
        }

        if(movie.getDuration() != null) {
            response.setDuration(DurationConverter.toTimeFormat(movie.getDuration()));
        }

        response.setBadge(movie.getBadge());
        response.setAccessType(movie.getAccessType());
        response.setReleaseYear(movie.getReleaseYear());

        response.setRating(movie.getRating() != null ? movie.getRating() : 0.0);

        // TAGS
        if (movie.getTags() != null) {
            response.setTags(
                    movie.getTags().stream().map(tag -> {
                        LookupResponse t = new LookupResponse();
                        t.setId(tag.getId());
                        t.setName(LocalizedText.getName(tag.getTitleEn(),tag.getTitleAr(),lang)); // TODO localize
                        return t;
                    }).toList()
            );
        }

        // LANGUAGE
        if (movie.getLanguage() != null) {
            response.setLanguage(movie.getLanguage().name());
        }

        // SUBTITLES
        if (movie.getSubtitles() != null) {
            response.setSubtitle(
                    movie.getSubtitles().stream()
                            .map(OriginalUploadMetadata::getGeneratedPath)
                            .toList()
            );
        }

        response.setSubtitleLanguages(movie.getSubtitleLanguages());
        response.setAudioLanguages(movie.getAudioLanguages());

        // ACTORS
        if (movie.getActors() != null) {
            response.setActors(
                    movie.getActors().stream().map(actor -> {
                        LookupPosterResponse a = new LookupPosterResponse();
                        a.setId(actor.getId());
                        a.setName(LocalizedText.getName(actor.getNameEn(),actor.getNameAr(),lang));
                        a.setPoster(actor.getGeneratedImagePath());
                        return a;
                    }).toList()
            );
        }

        response.setIsLike(isLke);
        response.setIsWatchList(isWatchList);

        // 3. RELATED MOVIES
        List<RelatedMovie> related = mapRelatedMovie(relatedMovies,lang);

        response.setRelatedMovies(related);

        return response;

    }

    private List<RelatedMovie> mapRelatedMovie(List<Movies> movies,String lang) {
        return movies.stream().map(m -> {
            RelatedMovie r = new RelatedMovie();
            r.setMovieId(m.getId());
            r.setTitle(LocalizedText.getName(m.getTitleEn(),m.getTitleAr(),lang));
            r.setBadge(m.getBadge());
            if (m.getPoster() != null) {
                r.setPoster(m.getPoster().getGeneratedPath());
            }

            return r;
        }).toList();
    }


}