package com.rand.ishowApi.movie.admin.application.service;


import com.rand.ishowApi.actor.domain.entity.Actor;
import com.rand.ishowApi.actor.domain.repository.ActorRepository;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.ActorMeta;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.mapper.MetaHelper;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.movie.admin.api.request.FilterMovieAdminRequest;
import com.rand.ishowApi.movie.admin.api.request.MovieAdminRequest;
import com.rand.ishowApi.movie.admin.api.response.MovieAdminFilterResponse;
import com.rand.ishowApi.movie.admin.api.response.MovieAdminSectionResponse;
import com.rand.ishowApi.movie.admin.application.manager.MovieDomainManager;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.movie.admin.domain.openSearch.index.MoviesIndex;
import com.rand.ishowApi.movie.admin.domain.repository.MoviesRepository;
import com.rand.ishowApi.movie.admin.domain.specification.MovieQueryBuilder;
import com.rand.ishowApi.movie.admin.domain.specification.MovieSpecification;
import com.rand.ishowApi.movie.admin.domain.specification.MovieSpecifications;
import com.rand.ishowApi.movie.admin.mapper.MovieAdminMapper;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.section.domain.repository.SectionRepository;
import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import com.rand.ishowApi.tag.admin.domain.repository.TagRepository;
import com.rand.ishowApi.upload.service.UploadServiceAsync;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MovieAdminService {
    private final MoviesRepository moviesRepository;
    private final ActorRepository actorRepository;
    private final TagRepository tagRepository;
    private final MovieDomainManager movieDomainManager;
    private final MovieAdminMapper movieMapper;
    private final UploadServiceAsync uploadServiceAsync;
    private final MongoTemplate mongoTemplate;
    private final SectionRepository sectionRepository;
    private final MetaHelper metaHelper;
    private final MovieOpenSearchService movieOpenSearchService;





    // manage movie mongo
    public Movies addMovie(MovieAdminRequest request) {

        List<Actor> actorsList = actorRepository.findAllById(request.getActors());
        List<Tag> tagList = tagRepository.findAllById(request.getTags());


        List<ActorMeta> actorMetaList=metaHelper.mapMetaActor(actorsList);
        List<TagMeta>tagMetaList =metaHelper.mapMetaTag(tagList);

        Movies movie = movieDomainManager.create(request, actorMetaList, tagMetaList);
        moviesRepository.save(movie);

        // 🔥 async calls (DO NOT block yet)
        CompletableFuture<OriginalUploadMetadata> originalFuture =
                uploadServiceAsync.uploadMovieOriginalVideoAsync(request.getFile(), movie.getId());

        CompletableFuture<OriginalUploadMetadata> posterFuture =
                uploadServiceAsync.uploadMoviePosterAsync(request.getPoster(), movie.getId());

        CompletableFuture<OriginalUploadMetadata> trailerFuture =
                uploadServiceAsync.uploadMovieTrailerAsync(request.getTrailer(), movie.getId());

        CompletableFuture<List<OriginalUploadMetadata>> subtitlesFuture =
                uploadServiceAsync.uploadMovieSubtitleAsync(request.getSubtitles(), movie.getId());

        // ✅ wait for ALL
        CompletableFuture.allOf(
                originalFuture, posterFuture, trailerFuture, subtitlesFuture
        ).join();

        // ✅ get results
        OriginalUploadMetadata originalFile = originalFuture.join();
        OriginalUploadMetadata poster = posterFuture.join();
        OriginalUploadMetadata trailer = trailerFuture.join();
        List<OriginalUploadMetadata> subtitles = subtitlesFuture.join();

        // ✅ set into entity
        movieDomainManager.addMovieFiles(movie, originalFile, poster, trailer, subtitles);

        return moviesRepository.save(movie);
    }
    public Movies findMovieById(String id) {
        return moviesRepository.findById(id)
                .orElseThrow(()-> new CustomException(ApiResponseCode.MOVIE_NOT_FOUND, null));
    }
    public Movies updateMovie(MovieAdminRequest request) throws IOException {
        // 1. Find existing movie
        Movies movie = findMovieById(request.getMovieId());

        // 2. Prepare actors and tags if provided
        List<Actor> actors = null;
        if (request.getActors() != null) {
            actors = actorRepository.findAllById(request.getActors());
        }
        List<Tag> tags = null;
        if (request.getTags() != null) {
            tags = tagRepository.findAllById(request.getTags());
        }

        // 3. Prepare file upload futures
        List<CompletableFuture<?>> futures = new ArrayList<>();
        CompletableFuture<OriginalUploadMetadata> originalFuture = null;
        CompletableFuture<OriginalUploadMetadata> posterFuture = null;
        CompletableFuture<OriginalUploadMetadata> trailerFuture = null;
        CompletableFuture<List<OriginalUploadMetadata>> subtitlesFuture = null;

        // File
        if (request.getFile() != null) {
            originalFuture = uploadServiceAsync.uploadMovieOriginalVideoAsync(request.getFile(), movie.getId());
            futures.add(originalFuture);
        }
        // Poster
        if (request.getPoster() != null) {
            posterFuture = uploadServiceAsync.uploadMoviePosterAsync(request.getPoster(), movie.getId());
            futures.add(posterFuture);
        }
        // Trailer
        if (request.getTrailer() != null) {
            trailerFuture = uploadServiceAsync.uploadMovieTrailerAsync(request.getTrailer(), movie.getId());
            futures.add(trailerFuture);
        }
        // Subtitles
        if (request.getSubtitles() != null) {
            subtitlesFuture = uploadServiceAsync.uploadMovieSubtitleAsync(request.getSubtitles(), movie.getId());
            futures.add(subtitlesFuture);
        }

        // Wait for all uploads to complete
        if (!futures.isEmpty()) {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }

        // Get results (or null if not uploaded)
        OriginalUploadMetadata originalMetadata = originalFuture != null ? originalFuture.join() : null;
        OriginalUploadMetadata posterMetadata = posterFuture != null ? posterFuture.join() : null;
        OriginalUploadMetadata trailerMetadata = trailerFuture != null ? trailerFuture.join() : null;
        List<OriginalUploadMetadata> subtitlesMetadata = subtitlesFuture != null ? subtitlesFuture.join() : null;

        // Remove old files if new metadata is present (or null meaning remove)
        if (originalMetadata != null || (request.getFile() != null)) {
            // If request.getFile() != null but originalMetadata is null (empty file), we want to remove old file
            if (movie.getOriginalUploadMetadata() != null) {
                uploadServiceAsync.removeOldFileAsync(movie.getOriginalUploadMetadata());
            }
        }
        if (posterMetadata != null || (request.getPoster() != null)) {
            if (movie.getPoster() != null) {
                uploadServiceAsync.removeOldFileAsync(movie.getPoster());
            }
        }
        if (trailerMetadata != null || (request.getTrailer() != null)) {
            if (movie.getTrailer() != null && movie.getTrailer().getOriginalUploadMetadata() != null) {
                uploadServiceAsync.removeOldFileAsync(movie.getTrailer().getOriginalUploadMetadata());
            }
        }
        if (subtitlesMetadata != null || (request.getSubtitles() != null)) {
            // Remove each old subtitle file
            if (movie.getSubtitles() != null) {
                for (OriginalUploadMetadata subtitle : movie.getSubtitles()) {
                    uploadServiceAsync.removeOldFileAsync(subtitle);
                }
            }
        }


        List<TagMeta> tag= metaHelper.mapMetaTag(tags);
        List<ActorMeta> actor= metaHelper.mapMetaActor(actors);
        // Update movie with new metadata (null means keep existing, but we already removed old files)
        movieDomainManager.update(movie, request, actor, tag,
                originalMetadata, posterMetadata, trailerMetadata, subtitlesMetadata);

        // update movie index
        if(movie.getIsPublish()) {
            movieOpenSearchService.updateMovieIndexes(movie);
        }
        // Save and return
        return moviesRepository.save(movie);
    }
    public Page<MovieAdminFilterResponse> filter(FilterMovieAdminRequest request) {

        List<MovieSpecification> specs = new ArrayList<>();

        if (request.getStatus() != null) {
            specs.add(MovieSpecifications.hasTranscodeStatus(request.getStatus()));
        }

        if (request.getAccessType() != null) {
            specs.add(MovieSpecifications.hasAccessType(request.getAccessType()));
        }

        if (request.getBadge() != null) {
            specs.add(MovieSpecifications.hasBadge(request.getBadge()));
        }

        if (request.getLanguage() != null) {
            specs.add(MovieSpecifications.hasLanguage(request.getLanguage()));
        }

        if (request.getActorId() != null) {
            specs.add(MovieSpecifications.hasActor(request.getActorId()));
        }

        if (request.getAccountId() != null) {
            specs.add(MovieSpecifications.createdOrUpdatedBy(request.getAccountId()));
        }

        // 🔁 String → Boolean
        if (request.getIsActive() != null) {
            specs.add(MovieSpecifications.isActive(BooleanConverter.getActiveBoolean(request.getIsActive())));
        }

        if (request.getIsPublish() != null) {
            specs.add(MovieSpecifications.isPublish(BooleanConverter.getActiveBoolean(request.getIsPublish())));
        }

        if (request.getIsKids() != null) {
            specs.add(MovieSpecifications.isKids(BooleanConverter.getActiveBoolean(request.getIsKids())));
        }

        if (request.getIsSport() != null) {
            specs.add(MovieSpecifications.isSports(BooleanConverter.getActiveBoolean(request.getIsSport())));
        }

        Query query = MovieQueryBuilder.build(specs);

        // ✅ TEXT SEARCH ONLY (no regex)
        if (request.getName() != null && !request.getName().isEmpty()) {
            TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                    .matching(request.getName());

            query.addCriteria(textCriteria);
        }

        int page = request.getPage() != null ? request.getPage() -1 : 0;
        int size = request.getSize() != null ? request.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size);

        long total = mongoTemplate.count(query, Movies.class);

        query.with(pageable);

        List<Movies> movies = mongoTemplate.find(query, Movies.class);

        return new PageImpl<>(
                movieMapper.toFilterResponse(movies),
                pageable,
                total
        );
    }
    //manage movie transcode
    public void updateMovieTranscodeResult(String movieId, TranscodeMetaData metaData) throws IOException {

        Movies movie = findMovieById(movieId);
        movieDomainManager.addMovieTranscodeFile(movie,metaData);
        moviesRepository.save(movie);

        if (movie.getIsPublish()){
           movieOpenSearchService.updateMovieIndexes(movie);
        }

    }

    public Movies publishMovie(String movieId) throws IOException {

        Movies movie = moviesRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.MOVIE_NOT_FOUND, null));

        if (movie.getTranscodeStatus() != TranscodeStatus.COMPLETED) {
            throw new CustomException(ApiResponseCode.MOVIE_TRANSCODE_NOT_COMPLETE, null);
        }

        movie.setIsPublish(true);

        movieOpenSearchService.updateSearchIndex(movie);
        return moviesRepository.save(movie);
    }




    public void removeMovieFromSection(Long sectionId, String movieId) {

        movieOpenSearchService.removeMovieFromSection(sectionId,movieId,MoviesIndex.MOVIES_SECTION.getIndexName());
    }
    public List<MovieAdminSectionResponse> getMoviesSection(Long sectionId, String isTop, int page, int size) throws IOException {
        return movieOpenSearchService.getMoviesSection(sectionId,MoviesIndex.MOVIES_SECTION.getIndexName(),isTop,page,size);
    }
    public void updateMovieIsTop(Long sectionId, String movieId, String isTop) throws IOException {
        movieOpenSearchService.updateMovieIsTop(sectionId,movieId,MoviesIndex.MOVIES_SECTION.getIndexName(),  isTop);
    }
    public void addMovieToSection(String movieId, Long sectionId, String isTop) throws IOException {
        Movies movies =findMovieById(movieId);
        Section section =sectionRepository.findById(sectionId).orElseThrow(()
       -> new CustomException(ApiResponseCode.SECTION_NOT_EXISTS, null) );

        movieOpenSearchService.addMovieToSection(movies,section,MoviesIndex.MOVIES_SECTION.getIndexName(),isTop );
    }
}
