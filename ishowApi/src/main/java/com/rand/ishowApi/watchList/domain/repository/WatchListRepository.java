package com.rand.ishowApi.watchList.domain.repository;

import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.watchList.domain.entity.WatchList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface WatchListRepository extends MongoRepository<WatchList, String> {
    Optional<WatchList> findByAccountIdAndContentIdAndContentType(Long accountId, String contentId, ContentType contentType);

    @Query(value = "{ 'accountId': ?0, 'contentId': ?1, 'contentType': ?2, 'active': true }", count = true)
    long countByAccountIdAndContentIdAndContentTypeAndActiveTrue(Long accountId, String contentId, ContentType contentType);

    Page<WatchList> findAllByAccountIdAndContentTypeAndActiveTrueOrderByCreatedAtDesc(Long accountId, ContentType contentType, Pageable pageable);

    List<WatchList> findAllByAccountIdAndActiveTrueOrderByCreatedAtDesc(Long accountId);

    List<WatchList> findAllByAccountIdAndContentTypeAndActiveTrueOrderByCreatedAtDesc(Long accountId, ContentType contentType);

    List<WatchList> findAllByAccountIdAndActiveTrueAndContentTypeOrderByCreatedAtDesc(Long accountId, ContentType contentType);
}


