package com.rand.ishowApi.favorite.domain.repository;

import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.favorite.domain.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;


public interface FavoriteRepository extends MongoRepository<Favorite, String> {
    Optional<Favorite> findByAccountIdAndContentIdAndContentType(Long accountId, String contentId, ContentType contentType);

    @Query(value = "{ 'accountId': ?0, 'contentId': ?1, 'contentType': ?2, 'active': true }", count = true)
    long countByAccountIdAndContentIdAndContentTypeAndActiveTrue(Long accountId, String contentId, ContentType contentType);

    Page<Favorite> findAllByAccountIdAndContentTypeAndActiveTrueOrderByCreatedAtDesc(Long accountId, ContentType contentType, Pageable pageable);

    List<Favorite> findAllByAccountIdAndActiveTrueOrderByCreatedAtDesc(Long accountId);

    List<Favorite> findAllByAccountIdAndContentTypeAndActiveTrueOrderByCreatedAtDesc(Long accountId, ContentType contentType);

    List<Favorite> findAllByAccountIdAndActiveTrueAndContentTypeOrderByCreatedAtDesc(Long accountId, ContentType contentType);
}



