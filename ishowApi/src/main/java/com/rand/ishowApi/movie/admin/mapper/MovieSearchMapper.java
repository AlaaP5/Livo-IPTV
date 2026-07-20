package com.rand.ishowApi.movie.admin.mapper;

import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.movie.admin.domain.openSearch.model.MovieSearchDocument;
import org.springframework.stereotype.Component;

@Component
public class MovieSearchMapper {

    public MovieSearchDocument toDoc(Movies movie) {
        if (movie == null) return null;

        MovieSearchDocument doc = new MovieSearchDocument();

        doc.setId(movie.getId());
        doc.setTitleEn(movie.getTitleEn());
        doc.setTitleAr(movie.getTitleAr());
        doc.setDescriptionEn(movie.getDescriptionEn());
        doc.setDescriptionAr(movie.getDescriptionAr());

        doc.setBadge(movie.getBadge());
        doc.setPoster(movie.getPoster());

        doc.setTags(movie.getTags());
        doc.setAccessType(movie.getAccessType());
        doc.setActors(movie.getActors());

        doc.setReleaseYear(movie.getReleaseYear());
        doc.setDuration(movie.getDuration());

        doc.setTranscodeMetaData(movie.getTranscodeMetaData());
        doc.setOriginalUploadMetadata(movie.getOriginalUploadMetadata());
        doc.setSubtitles(movie.getSubtitles());

        doc.setSubtitleLanguages(movie.getSubtitleLanguages());
        doc.setAudioLanguages(movie.getAudioLanguages());

        doc.setTrailer(movie.getTrailer());
        doc.setTranscodeStatus(movie.getTranscodeStatus());

        doc.setIsPublish(movie.getIsPublish());
        doc.setHasRight(movie.getHasRight());
        doc.setLanguage(movie.getLanguage());

        doc.setActive(movie.getActive());
        doc.setIsKids(movie.getIsKids());
        doc.setIsSports(movie.getIsSports());

        doc.setRating(movie.getRating());

        return doc;
    }
}