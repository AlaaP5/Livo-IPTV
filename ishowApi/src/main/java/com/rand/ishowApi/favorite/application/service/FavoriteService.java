package com.rand.ishowApi.favorite.application.service;

import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.account.mobile.application.service.AccountMobileService;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.favorite.api.request.FavoriteRequest;
import com.rand.ishowApi.favorite.api.response.FavoriteItemResponse;
import com.rand.ishowApi.favorite.application.manager.FavoriteDomainManager;
import com.rand.ishowApi.favorite.domain.entity.Favorite;
import com.rand.ishowApi.favorite.domain.repository.FavoriteRepository;
import com.rand.ishowApi.favorite.mapper.FavoriteMapper;
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
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final FavoriteDomainManager favoriteDomainManager;
    private final AccountMobileService accountService;

    @Transactional
    public void addToFavorite(FavoriteRequest request) {
        Account account = accountService.getCurrentAccount();

        favoriteMapper.findContent(request.getContentType(), request.getContentId())
                .orElseThrow(() -> new CustomException(favoriteMapper.notFoundCode(request.getContentType()), null));

        Favorite favorite = favoriteRepository
                .findByAccountIdAndContentIdAndContentType(account.getId(), request.getContentId(), request.getContentType())
                .map(favoriteDomainManager::reactivate)
                .orElseGet(() -> favoriteDomainManager.create(account.getId(), request.getContentId(), request.getContentType()));

        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFromFavorite(FavoriteRequest request) {
        Account account = accountService.getCurrentAccount();

        Favorite favorite = favoriteRepository
                .findByAccountIdAndContentIdAndContentType(account.getId(), request.getContentId(), request.getContentType())
                .orElseThrow(() -> new CustomException(ApiResponseCode.FAVORITE_NOT_FOUND, null));


        favoriteRepository.delete(favorite);
    }

    public boolean isInFavorite(String contentId, String contentType) {
        ContentType type =  ContentType.fromValue(contentType);
        return isInFavorite(contentId, type);
    }

    public boolean isInFavorite(String contentId, ContentType contentType) {
        Account account = accountService.getCurrentAccount();
        return favoriteRepository.countByAccountIdAndContentIdAndContentTypeAndActiveTrue(account.getId(), contentId, contentType) > 0;
    }

    public Page<FavoriteItemResponse<?>> getFavorite(String contentType, int page, int size) {

        ContentType type = ContentType.fromValue(contentType);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);

        Account account = accountService.getCurrentAccount();

        Page<Favorite> favorites = favoriteRepository.findAllByAccountIdAndContentTypeAndActiveTrueOrderByCreatedAtDesc(account.getId(), type, pageable);

        if (favorites == null || favorites.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        return new PageImpl<>(favoriteMapper.map(favorites.getContent()), pageable, favorites.getTotalElements());
    }
}


