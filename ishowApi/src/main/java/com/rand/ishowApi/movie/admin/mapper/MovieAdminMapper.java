package com.rand.ishowApi.movie.admin.mapper;


import com.rand.ishowApi.movie.admin.api.response.MovieAdminFilterResponse;
import com.rand.ishowApi.movie.admin.api.response.MovieAdminSectionResponse;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.movie.admin.domain.openSearch.model.MovieDocument;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieAdminMapper {


  public  List<MovieAdminFilterResponse> toFilterResponse(List<Movies> movies){
        return movies.stream().map(movie -> {
            MovieAdminFilterResponse response = new MovieAdminFilterResponse();
            response.setMovieId(movie.getId());
            response.setTitleEn(movie.getTitleEn());
            response.setTitleAr(movie.getTitleAr());
            response.setDescriptionEn(movie.getDescriptionEn());
            response.setDescriptionAr(movie.getDescriptionAr());
            response.setPoster(movie.getPoster());
            response.setReleaseYear(movie.getReleaseYear());
            response.setDuration(movie.getDuration());
            response.setRating(movie.getRating());
            response.setBadge(movie.getBadge());
            response.setLanguage(movie.getLanguage());
            response.setAccessType(movie.getAccessType());
            response.setHasRight(movie.getHasRight());
            response.setIsKids(movie.getIsKids());
            response.setActive(movie.getActive());
            response.setIsSports(movie.getIsSports());
            response.setTranscodeStatus(movie.getTranscodeStatus());
            response.setIsPublish(movie.getIsPublish());
            response.setSubtitleLanguages(movie.getSubtitleLanguages());
            response.setAudioLanguages(movie.getAudioLanguages());
            response.setTags(movie.getTags());
            return response;
        }).collect(Collectors.toList());
    }

    public MovieAdminSectionResponse toMovieSectionResponse(MovieDocument document) {
        if (document == null) {
            return null;
        }
        MovieAdminSectionResponse response = new MovieAdminSectionResponse();
        response.setMovieId(document.getMovieId());
        response.setTitleEn(document.getTitleEn());
        response.setTitleAr(document.getTitleAr());
        response.setPoster(document.getPoster());
        response.setCreateDate(document.getCreateDate());
        response.setIsTop(document.getIsTop());
        response.setIsKids(document.getIsKids());
        response.setIsSports(document.getIsSports());
        response.setIsPublish(document.getIsPublish());

        response.setSectionId(document.getSectionId());
        response.setSectionTitleAr(document.getSectionTitleAr());
        response.setSectionTitleEn(document.getSectionTitleEn());
        response.setSectionOrder(document.getSectionOrder());
        response.setSectionActive(document.isSectionActive());
        response.setSectionPublish(document.isSectionPublish());

        return response;
    }

    public List<MovieAdminSectionResponse> toMovieSectionResponseList(List<MovieDocument> documents) {
        return documents.stream()
                .map(this::toMovieSectionResponse)
                .collect(Collectors.toList());
    }
}
