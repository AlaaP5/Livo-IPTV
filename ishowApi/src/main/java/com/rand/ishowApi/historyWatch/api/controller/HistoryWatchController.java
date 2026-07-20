package com.rand.ishowApi.historyWatch.api.controller;

import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.historyWatch.api.request.HistoryWatchRequest;
import com.rand.ishowApi.historyWatch.api.response.HistoryWatchItemResponse;
import com.rand.ishowApi.historyWatch.application.service.HistoryWatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/mobile/history/")
@RequiredArgsConstructor
public class HistoryWatchController {
    private final HistoryWatchService historyWatchService;

    @PostMapping("add")
    public ApiResponse<Void> addToHistoryWatch(@Valid @RequestBody HistoryWatchRequest request) {
        historyWatchService.addToHistoryWatch(request);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("exists")
    public ApiResponse<Boolean> isInHistoryWatch(@RequestParam String contentId,
                                              @RequestParam String contentType) {
        return ApiResponse.success(historyWatchService.isInHistoryWatch(contentId, contentType), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("list")
    public ApiResponse<List<HistoryWatchItemResponse<?>>> getHistoryWatch(@RequestParam String contentType,
                                                                           @RequestParam(defaultValue = "1") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        Page<HistoryWatchItemResponse<?>> result = historyWatchService.getHistoryWatch(contentType, page, size);
        return ApiResponse.success(result.getContent(), ApiResponseCode.SUCCESS_200, result.getTotalElements(), result.getNumber() + 1, result.getSize());
    }
}

