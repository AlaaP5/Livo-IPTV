
package com.rand.ishowApi.movie.admin.application.manager;

import com.rand.ishowApi.metadata.ActorMeta;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.Trailer;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.movie.admin.api.request.MovieAdminRequest;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieDomainManager {

    public Movies create(MovieAdminRequest req, List<ActorMeta> actors, List<TagMeta> tags) {
        return Movies.builder()
                .titleEn(req.getTitleEn())
                .titleAr(req.getTitleAr())
                .descriptionEn(req.getDescriptionEn())
                .descriptionAr(req.getDescriptionAr())
                .badge(req.getBadge())
                .tags(tags)
                .accessType(req.getAccessType())
                .actors(actors)
                .releaseYear(req.getReleaseYear())
                .hasRight(BooleanConverter.getActiveBoolean(req.getHasRight()))
                .language(req.getLanguage())
                .isKids(BooleanConverter.getActiveBoolean(req.getIsKids()))
                .isSports(BooleanConverter.getActiveBoolean(req.getIsSports()))
                .duration(req.getDuration())
                .rating(req.getRating())
                .transcodeStatus(TranscodeStatus.PENDING)
                .isPublish(false)
                .active(BooleanConverter.getActiveBoolean(req.getActive()))
                .subtitleLanguages(req.getSubtitleLanguages())
                .audioLanguages(req.getAudioLanguages())
                .build();
    }

      public void addMovieFiles(Movies movie, OriginalUploadMetadata file, OriginalUploadMetadata poster, OriginalUploadMetadata trailer, List<OriginalUploadMetadata> subtitles ){

          movie.setOriginalUploadMetadata(file);
          movie.setPoster(poster);
          Trailer t=new Trailer();
          t.setOriginalUploadMetadata(trailer);
          t.setIsPublish(false);
          t.setTranscodeStatus(TranscodeStatus.PENDING);
          movie.setTrailer(t);
          movie.setSubtitles(subtitles);
      }

      public void addMovieTranscodeFile(Movies movies, TranscodeMetaData transcodeMetaData){

        movies.setTranscodeMetaData(transcodeMetaData);
        movies.setTranscodeStatus(TranscodeStatus.COMPLETED);
      }

      public void addTrailerTranscodeFile(Movies movies,TranscodeMetaData transcodeMetaData){
        movies.getTrailer().setTranscodeMetaData(transcodeMetaData);
          movies.getTrailer().setTranscodeStatus(TranscodeStatus.COMPLETED);

      }

      public void publishMovie(Movies movie){
        movie.setIsPublish(true);
      }

        public void deactivateMovie(Movies movie,String active){
            movie.setActive(BooleanConverter.getActiveBoolean(active));
        }


       public void update(Movies movie, MovieAdminRequest request,
                            List<ActorMeta> actors, List<TagMeta> tags ,
                            OriginalUploadMetadata originalVideo,
                            OriginalUploadMetadata poster,
                            OriginalUploadMetadata trailer,
                            List<OriginalUploadMetadata> subtitles) {
             if (request.getTitleEn() != null) {
                 movie.setTitleEn(request.getTitleEn());
             }
             if (request.getTitleAr() != null) {
                 movie.setTitleAr(request.getTitleAr());
             }
             if (request.getDescriptionEn() != null) {
                 movie.setDescriptionEn(request.getDescriptionEn());
             }
             if (request.getDescriptionAr() != null) {
                 movie.setDescriptionAr(request.getDescriptionAr());
             }
             if (request.getBadge() != null) {
                 movie.setBadge(request.getBadge());
             }
             if (request.getAccessType() != null) {
                 movie.setAccessType(request.getAccessType());
             }
             if (request.getReleaseYear() != null) {
                 movie.setReleaseYear(request.getReleaseYear());
             }
             if (request.getHasRight() != null) {
                 movie.setHasRight(BooleanConverter.getActiveBoolean(request.getHasRight()));
             }
             if (request.getLanguage() != null) {
                 movie.setLanguage(request.getLanguage());
             }
             if (request.getIsKids() != null) {
                 movie.setIsKids(BooleanConverter.getActiveBoolean(request.getIsKids()));
             }
             if (request.getIsSports() != null) {
                 movie.setIsSports(BooleanConverter.getActiveBoolean(request.getIsSports()));
             }
             if (request.getRating() != null) {
                 movie.setRating(request.getRating());
             }
              if (request.getDuration() != null) {
                  movie.setDuration(request.getDuration());
              }
             if (actors != null) {
                 movie.setActors(actors);
             }
             if (tags != null) {
                 movie.setTags(tags);
             }
             if (request.getActive() != null) {
                 movie.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
             }

             //
             if (originalVideo != null) {
                 movie.setOriginalUploadMetadata(originalVideo);
             }
             if (poster != null) {
                 movie.setPoster(poster);
             }
             if (trailer != null) {
                 if (movie.getTrailer() == null) {
                     movie.setTrailer(new Trailer());
                 }
                 movie.getTrailer().setOriginalUploadMetadata(trailer);
             }
             if (subtitles != null) {
                 movie.setSubtitles(subtitles);
             }
              if (request.getSubtitleLanguages() != null) {
                  movie.setSubtitleLanguages(request.getSubtitleLanguages());
              }
              if (request.getAudioLanguages() != null) {
                  movie.setAudioLanguages(request.getAudioLanguages());
              }

     }






}

