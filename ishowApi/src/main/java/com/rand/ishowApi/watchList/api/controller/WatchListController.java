package com.rand.ishowApi.watchList.api.controller;

import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.watchList.api.request.WatchListRequest;
import com.rand.ishowApi.watchList.api.response.WatchListItemResponse;
import com.rand.ishowApi.watchList.application.service.WatchListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/mobile/watch-list/")
@RequiredArgsConstructor
public class WatchListController {
    private final WatchListService watchListService;

    @PostMapping("add")
    public ApiResponse<Void> addToWatchList(@Valid @RequestBody WatchListRequest request) {
        watchListService.addToWatchList(request);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @DeleteMapping("remove")
    public ApiResponse<Void> removeFromWatchList(@Valid @RequestBody WatchListRequest request) {
        watchListService.removeFromWatchList(request);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("exists")
    public ApiResponse<Boolean> isInWatchList(@RequestParam String contentId,
                                              @RequestParam String contentType) {
        return ApiResponse.success(watchListService.isInWatchList(contentId, contentType), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("list")
    public ApiResponse<List<WatchListItemResponse<?>>> getWatchList(@RequestParam String contentType,
                                                                    @RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Page<WatchListItemResponse<?>> result = watchListService.getWatchList(contentType, page, size);
        return ApiResponse.success(result.getContent(), ApiResponseCode.SUCCESS_200, result.getTotalElements(), result.getNumber() + 1, result.getSize());
    }
}

