package com.rand.ishowApi.watchList.application.service;

import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.account.mobile.application.service.AccountMobileService;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.watchList.api.request.WatchListRequest;
import com.rand.ishowApi.watchList.api.response.WatchListItemResponse;
import com.rand.ishowApi.watchList.application.manager.WatchListDomainManager;
import com.rand.ishowApi.watchList.domain.entity.WatchList;
import com.rand.ishowApi.watchList.domain.repository.WatchListRepository;
import com.rand.ishowApi.watchList.mapper.WatchListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class WatchListService {
    private final WatchListRepository watchListRepository;
    private final WatchListMapper watchListMapper;
    private final WatchListDomainManager watchListDomainManager;
    private final AccountMobileService accountService;

    @Transactional
    public void addToWatchList(WatchListRequest request) {
        Account account = accountService.getCurrentAccount();

        watchListMapper.findContent(request.getContentType(), request.getContentId())
                .orElseThrow(() -> new CustomException(watchListMapper.notFoundCode(request.getContentType()), null));

        WatchList watchList = watchListRepository
                .findByAccountIdAndContentIdAndContentType(account.getId(), request.getContentId(), request.getContentType())
                .map(watchListDomainManager::reactivate)
                .orElseGet(() -> watchListDomainManager.create(account.getId(), request.getContentId(), request.getContentType()));

        watchListRepository.save(watchList);
    }

    @Transactional
    public void removeFromWatchList(WatchListRequest request) {
        Account account = accountService.getCurrentAccount();

        WatchList watchList = watchListRepository
                .findByAccountIdAndContentIdAndContentType(account.getId(), request.getContentId(), request.getContentType())
                .orElseThrow(() -> new CustomException(ApiResponseCode.WATCHLIST_NOT_FOUND, null));


        watchListRepository.delete(watchList);
    }

    public boolean isInWatchList(String contentId, String contentType) {
        ContentType type =  ContentType.fromValue(contentType);
        return isInWatchList(contentId, type);
    }

    public boolean isInWatchList(String contentId, ContentType contentType) {
        Account account = accountService.getCurrentAccount();
        return watchListRepository.countByAccountIdAndContentIdAndContentTypeAndActiveTrue(account.getId(), contentId, contentType) > 0;
    }

    public Page<WatchListItemResponse<?>> getWatchList(String contentType, int page, int size) {

        ContentType type = ContentType.fromValue(contentType);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);

        Account account = accountService.getCurrentAccount();

        Page<WatchList> watchLists = watchListRepository.findAllByAccountIdAndContentTypeAndActiveTrueOrderByCreatedAtDesc(account.getId(), type, pageable);

        if (watchLists == null || watchLists.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        return new PageImpl<>(watchListMapper.map(watchLists.getContent()), pageable, watchLists.getTotalElements());
    }
}


