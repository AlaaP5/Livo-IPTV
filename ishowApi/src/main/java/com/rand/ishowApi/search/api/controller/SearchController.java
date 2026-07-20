package com.rand.ishowApi.search.api.controller;


import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.search.api.request.SearchRequest;
import com.rand.ishowApi.search.api.response.SearchResponse;
import com.rand.ishowApi.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/mobile/search/")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;


    @PostMapping("all")
    public ApiResponse<SearchResponse> search(@RequestBody SearchRequest searchRequest) throws IOException {
        return  ApiResponse.success(searchService.search(searchRequest), ApiResponseCode.SUCCESS_200) ;
    }



}
