package com.rand.ishowApi.library.api.controller;

import com.rand.ishowApi.library.api.response.LibraryResponse;
import com.rand.ishowApi.library.application.service.LibraryService;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mobile/library/")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @GetMapping("list")
    public ApiResponse<LibraryResponse> getMyLibrary(@RequestParam String contentType,
                                                     @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.success(libraryService.getMyLibrary(contentType, page, size), ApiResponseCode.SUCCESS_200);
    }
}

