package com.rand.ishowApi.favorite.api.controller;

import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.favorite.api.request.FavoriteRequest;
import com.rand.ishowApi.favorite.api.response.FavoriteItemResponse;
import com.rand.ishowApi.favorite.application.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/mobile/favorite/")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("add")
    public ApiResponse<Void> addToFavorite(@Valid @RequestBody FavoriteRequest request) {
        favoriteService.addToFavorite(request);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @DeleteMapping("remove")
    public ApiResponse<Void> removeFromFavorite(@Valid @RequestBody FavoriteRequest request) {
        favoriteService.removeFromFavorite(request);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("exists")
    public ApiResponse<Boolean> isInFavorite(@RequestParam String contentId,
                                              @RequestParam String contentType) {
        return ApiResponse.success(favoriteService.isInFavorite(contentId, contentType), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("list")
    public ApiResponse<List<FavoriteItemResponse<?>>> getFavorite(@RequestParam String contentType,
                                                                    @RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Page<FavoriteItemResponse<?>> result = favoriteService.getFavorite(contentType, page, size);
        return ApiResponse.success(result.getContent(), ApiResponseCode.SUCCESS_200, result.getTotalElements(), result.getNumber() + 1, result.getSize());
    }
}


