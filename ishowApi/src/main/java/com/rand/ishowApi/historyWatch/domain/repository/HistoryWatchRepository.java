package com.rand.ishowApi.historyWatch.domain.repository;

import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.historyWatch.domain.entity.HistoryWatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface HistoryWatchRepository extends MongoRepository<HistoryWatch, String> {
    Optional<HistoryWatch> findByAccountIdAndContentIdAndContentType(Long accountId, String contentId, ContentType contentType);

    @Query(value = "{ 'accountId': ?0, 'contentId': ?1, 'contentType': ?2, 'active': true }", count = true)
    long countByAccountIdAndContentIdAndContentTypeAndActiveTrue(Long accountId, String contentId, ContentType contentType);

    Page<HistoryWatch> findAllByAccountIdAndContentTypeAndActiveTrueOrderByCreatedAtDesc(Long accountId, ContentType contentType, Pageable pageable);

    List<HistoryWatch> findAllByAccountIdAndActiveTrueOrderByCreatedAtDesc(Long accountId);

    List<HistoryWatch> findAllByAccountIdAndContentTypeAndActiveTrueOrderByCreatedAtDesc(Long accountId, ContentType contentType);

    List<HistoryWatch> findAllByAccountIdAndActiveTrueAndContentTypeOrderByCreatedAtDesc(Long accountId, ContentType contentType);
}

