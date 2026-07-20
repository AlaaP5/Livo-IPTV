package com.rand.ishowApi.movie.admin.application.manager;


import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.movie.admin.domain.openSearch.model.MovieDocument;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class MovieOpenSearchManager {

    public MovieDocument createMovieSectionDocument(Movies movie, Section section, String isTop) {
        MovieDocument doc = new MovieDocument();
        doc.setSectionId(section.getId());
        doc.setSectionTitleAr(section.getTitleAr());
        doc.setSectionTitleEn(section.getTitleEn());
        doc.setSectionOrder(section.getOrder());
        doc.setSectionActive(section.getActive());
        doc.setSectionPublish(section.getPublish());
        doc.setMovieId(movie.getId());
        doc.setTitleEn(movie.getTitleEn());
        doc.setTitleAr(movie.getTitleAr());
        doc.setDescriptionEn(movie.getDescriptionEn());
        doc.setDescriptionAr(movie.getDescriptionAr());
        doc.setPoster(movie.getPoster());
        doc.setBadge(movie.getBadge());
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
        doc.setIsTop(false); // Default value, can be overridden if needed
        doc.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        doc.setIsTop(BooleanConverter.getActiveBoolean(isTop));
        return doc;
    }

    public void updateMovieSectionDocument(MovieDocument doc, Movies movie){
        doc.setTitleEn(movie.getTitleEn());
        doc.setTitleAr(movie.getTitleAr());
        doc.setDescriptionEn(movie.getDescriptionEn());
        doc.setDescriptionAr(movie.getDescriptionAr());
        doc.setPoster(movie.getPoster());
        doc.setBadge(movie.getBadge());
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
    }


    public void setIsTop(MovieDocument doc, String isTop){
        doc.setIsTop(BooleanConverter.getActiveBoolean(isTop) );
    }
}
