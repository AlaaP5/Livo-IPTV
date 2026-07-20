package com.rand.ishowApi.movie.admin.application.service;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.movie.admin.api.response.MovieAdminSectionResponse;
import com.rand.ishowApi.movie.admin.application.manager.MovieOpenSearchManager;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.movie.admin.domain.openSearch.index.MoviesIndex;
import com.rand.ishowApi.movie.admin.domain.openSearch.model.MovieDocument;
import com.rand.ishowApi.movie.admin.domain.openSearch.model.MovieSearchDocument;
import com.rand.ishowApi.movie.admin.mapper.MovieAdminMapper;
import com.rand.ishowApi.movie.admin.mapper.MovieSearchMapper;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.utils.BooleanConverter;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.query_dsl.BoolQuery;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MovieOpenSearchService {

    private final OpenSearchClient openSearchClient;
    private final MovieOpenSearchManager manager;
    private  final MovieAdminMapper mapper;
    private  final MovieSearchMapper searchMapper;


    //update movie indexes
    public void updateMovieIndexes(Movies movie) throws IOException {
        updateAllSectionIndexes(movie);
        updateSearchIndex(movie);
    }
  /*  public void updateMovieSectionIndex(Movies movie,String indexName) throws IOException {
        try {
            SearchResponse<MovieDocument> response =
                    openSearchClient.search(s -> s
                                    .index(MoviesIndex.MOVIES_SECTION.getIndexName())
                                    .query(q -> q
                                            .term(t -> t
                                                    .field("movieId")
                                                    .value(FieldValue.of(movie.getId()))
                                            )
                                    ),
                            MovieDocument.class
                    );
            List<MovieDocument> result = response.hits()
                    .hits()
                    .stream()
                    .map(Hit::source)
                    .toList();

            for (Hit<MovieDocument> hit : response.hits().hits()) {
                String docId = hit.id();
                MovieDocument doc = hit.source();
                if (doc == null) continue;
                manager.updateMovieSectionDocument(doc, movie);
                openSearchClient.update(u -> u
                                .index(MoviesIndex.MOVIES_SECTION.getIndexName())
                                .id(docId)
                                .doc(doc),
                        MovieDocument.class
                );
            }

        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.MOVIE_SECTION_INDEX_UPDATE_FAILED, null);
        }

    }*/

    public void updateAllSectionIndexes(Movies movie) {

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (MoviesIndex index : MoviesIndex.getSectionIndexes()) {

            futures.add(
                    updateMovieSectionIndex(
                            movie,
                            index.getIndexName()
                    )
            );
        }

        CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        ).join();
    }


    @Async
    public CompletableFuture<Void> updateMovieSectionIndex(
            Movies movie,
            String indexName
    ) {

        try {

            SearchResponse<MovieDocument> response =
                    openSearchClient.search(s -> s
                                    .index(indexName)
                                    .query(q -> q
                                            .term(t -> t
                                                    .field("movieId")
                                                    .value(FieldValue.of(movie.getId()))
                                            )
                                    ),
                            MovieDocument.class
                    );

            for (Hit<MovieDocument> hit : response.hits().hits()) {

                String docId = hit.id();
                MovieDocument doc = hit.source();

                if (doc == null) {
                    continue;
                }

                manager.updateMovieSectionDocument(doc, movie);

                openSearchClient.update(u -> u
                                .index(indexName)
                                .id(docId)
                                .doc(doc),
                        MovieDocument.class
                );
            }

            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            throw new CustomException(
                    ApiResponseCode.MOVIE_SECTION_INDEX_UPDATE_FAILED,
                    null
            );
        }
    }

    public void updateSearchIndex(Movies movie ) throws IOException {
        try {
            MovieSearchDocument document = searchMapper.toDoc(movie);

            openSearchClient.index(i -> i
                    .index(MoviesIndex.MOVIES_SEARCH.getIndexName())
                    .id(document.getId())
                    .document(document)
            );

        } catch (Exception e) {
            throw new CustomException(ApiResponseCode.MOVIE_SEARCH_INDEX_UPDATE_FAILED, null);
        }


    }    // manage movie section
    public List<MovieAdminSectionResponse> GetMoviesSection(Long sectionId, String indexName) throws IOException {
        SearchResponse<MovieDocument> response =
                openSearchClient.search(s -> s
                                .index(indexName)
                                .query(q -> q
                                        .term(t -> t
                                                .field("sectionId") // important
                                                .value(FieldValue.of(sectionId))
                                        )
                                ),
                        MovieDocument.class
                );
        List<MovieDocument> result = response.hits()
                .hits()
                .stream()
                .map(Hit::source)
                .toList();

        return mapper.toMovieSectionResponseList(result);
    }
    public List<MovieAdminSectionResponse> getMoviesSection(Long sectionId, String indexName, String isTop, int page, int size) throws IOException {
        page = page-1;
        int from = page * size;

        SearchResponse<MovieDocument> response = openSearchClient.search(s -> {
                    BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

                    boolBuilder.must(m -> m.term(t -> t
                            .field("sectionId")
                            .value(FieldValue.of(sectionId))
                    ));

                    if (isTop != null) {
                        boolBuilder.must(m -> m.term(t -> t
                                .field("isTop")
                                .value(FieldValue.of(BooleanConverter.getActiveBoolean(isTop)))
                        ));
                    }

                    return s.index(indexName)
                            .query(q -> q.bool(boolBuilder.build()))
                            .from(from)
                            .size(size);
                },
                MovieDocument.class
        );

        List<MovieDocument> result = response.hits()
                .hits()
                .stream()
                .map(Hit::source)
                .toList();

        return mapper.toMovieSectionResponseList(result);
    }
    public void removeMovieFromSection(Long sectionId, String movieId,String indexName ) {
        try {
            openSearchClient.deleteByQuery(b -> b
                    .index(indexName)
                    .query(q -> q
                            .bool(bool -> bool
                                    .must(m1 -> m1
                                            .term(t -> t
                                                    .field("sectionId")
                                                    .value(FieldValue.of(sectionId))
                                            )
                                    )
                                    .must(m2 -> m2
                                            .term(t -> t
                                                    .field("movieId")
                                                    .value(FieldValue.of(movieId))
                                            )
                                    )
                            )
                    )
            );
        } catch (Exception e){
            throw new CustomException(ApiResponseCode.MOVIE_DELETE_FROM_SECTION_FAILED, null);
        }
    }
    public void addMovieToSection(Movies movie,Section section,String indexName ,String isTop) throws IOException {

        if (movie.getTranscodeStatus()!= TranscodeStatus.COMPLETED )
        {
            throw new CustomException(ApiResponseCode.MOVIE_TRANSCODE_NOT_COMPLETE, null);
        }

        if (!movie.getIsPublish())
        {
            throw new CustomException(ApiResponseCode.MOVIE_NOT_PUBLISH, null);
        }
        var searchResponse = openSearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .must(m1 -> m1.term(t -> t.field("sectionId").value(FieldValue.of(section.getId()))))
                                        .must(m2 -> m2.term(t -> t.field("movieId").value(FieldValue.of(movie.getId()))))
                                )
                        )
                        .size(1)
                , MovieDocument.class);

        if (searchResponse.hits().hits().size()>0) {
            String docId = searchResponse.hits().hits().getFirst().id();

            MovieDocument doc = searchResponse.hits().hits().getFirst().source();

            if(doc == null)
                throw new CustomException(ApiResponseCode.MOVIE_NOT_FOUND, null);

            manager.updateMovieSectionDocument(doc,movie);
            manager.setIsTop(doc,isTop);
            openSearchClient.update(u -> u
                            .index(indexName)
                            .id(docId)
                            .doc(doc),
                    MovieDocument.class
            );
        }
        else
        {

            MovieDocument movieSectionDocument = manager.createMovieSectionDocument(movie, section,isTop);
            openSearchClient.index(i -> i
                    .index(indexName)
                    .document(movieSectionDocument)
            );

        }

    }
    public void updateMovieIsTop(Long sectionId,String movieId ,String indexName,String isTop) throws IOException {

        var searchResponse = openSearchClient.search(s -> s
                        .index(indexName)
                        .query(q -> q
                                .bool(b -> b
                                        .must(m1 -> m1.term(t -> t.field("sectionId").value(FieldValue.of(sectionId))))
                                        .must(m2 -> m2.term(t -> t.field("movieId").value(FieldValue.of(movieId))))
                                )
                        )
                        .size(1)
                , MovieDocument.class);

        if (searchResponse.hits().hits().isEmpty()) {
            throw new CustomException(ApiResponseCode.MOVIE_NOT_FOUND, null);
        }

        var hit = searchResponse.hits().hits().getFirst();

        String docId = hit.id();
        MovieDocument doc = hit.source();

        // update field
        manager.setIsTop(doc, isTop);

        openSearchClient.update(u -> u
                        .index(indexName)
                        .id(docId)
                        .doc(doc),
                MovieDocument.class
        );
    }


}
