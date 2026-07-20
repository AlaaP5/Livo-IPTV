package com.rand.ishowApi.movie.mobile.service;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.favorite.application.service.FavoriteService;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.movie.admin.domain.openSearch.index.MoviesIndex;
import com.rand.ishowApi.movie.admin.domain.openSearch.model.MovieDocument;
import com.rand.ishowApi.movie.admin.domain.repository.MoviesRepository;
import com.rand.ishowApi.movie.admin.domain.specification.MovieQueryBuilder;
import com.rand.ishowApi.movie.admin.domain.specification.MovieSpecification;
import com.rand.ishowApi.movie.admin.domain.specification.MovieSpecifications;
import com.rand.ishowApi.movie.mobile.api.response.MovieDetailsResponse;
import com.rand.ishowApi.movie.mobile.api.response.MovieSectionResponse;
import com.rand.ishowApi.movie.mobile.mapper.MovieDocMapper;
import com.rand.ishowApi.movie.mobile.mapper.MoviesMobileMapper;
import com.rand.ishowApi.openSearch.response.SectionBannerResponse;
import com.rand.ishowApi.openSearch.service.GenericSectionService;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.watchList.application.service.WatchListService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieMobileService {
    private final MoviesMobileMapper mapper;
    private final GenericSectionService sectionService;
    private final MovieDocMapper movieMapper;
    private final MongoTemplate mongoTemplate;
    private final MoviesRepository moviesRepository;
    private final WatchListService watchListService;
    private final FavoriteService favoriteService;

    public SectionBannerResponse<MovieSectionResponse> getMoviesSections() throws IOException {

        return sectionService.getSections(
                MoviesIndex.MOVIES_SECTION.getIndexName(),
                MovieDocument.class,
                ContentType.MOVIES,
                movieMapper,
                true
        );
    }

    public List<MovieSectionResponse> getMovieSectionById(Long sectionId) throws IOException {
        return sectionService.getSectionContent(
                MoviesIndex.MOVIES_SECTION.getIndexName(),
                MovieDocument.class,
                movieMapper,
                sectionId,
                true
        );
    }
   public MovieDetailsResponse getMovieDetails(String movieId) {
       Movies movie = moviesRepository.findById(movieId)
               .orElseThrow(() -> new CustomException(ApiResponseCode.MOVIE_NOT_FOUND, null));

       List<Movies> related = getRelatedMovies(movie);

       boolean isLke = favoriteService.isInFavorite(movieId, ContentType.MOVIES);
       boolean isWatchList = watchListService.isInWatchList(movieId, ContentType.MOVIES);

       return mapper.toMovieDetails(movie,related,isLke,isWatchList);
   }

       private List<Movies> getRelatedMovies(Movies movie) {

           List<Long> tagIds= movie.getTags()
                   .stream()
                   .map(TagMeta::getId)
                   .toList();

           List<MovieSpecification> specs = List.of(
                   MovieSpecifications.isActive(true),
                   MovieSpecifications.isPublish(true),
                   MovieSpecifications.hasLanguage(movie.getLanguage()),
                   MovieSpecifications.isKids(movie.getIsKids()),
                   MovieSpecifications.isSports(movie.getIsSports()),
                   MovieSpecifications.hasAnyTagIds(tagIds),
                   MovieSpecifications.excludeMovie(movie.getId())
           );

           Query query = MovieQueryBuilder.build(specs);

           query.with(Sort.by(Sort.Direction.DESC, "rating"));
           query.limit(5);

           return mongoTemplate.find(query, Movies.class);


       }
}
