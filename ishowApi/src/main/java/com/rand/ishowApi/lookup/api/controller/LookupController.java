package com.rand.ishowApi.lookup.api.controller;


import com.rand.ishowApi.lookup.api.response.LookupEnumResponse;
import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.lookup.api.response.MongoLookupResponse;
import com.rand.ishowApi.lookup.application.service.LookupService;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lookup")
@RequiredArgsConstructor
public class LookupController {

    private final LookupService lookupService;

    @GetMapping("/mobile-page")
    public ApiResponse<List<LookupEnumResponse>> getMobilePage() {
        return ApiResponse.success(lookupService.getMobilePage(), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/content-type")
    public ApiResponse<List<LookupEnumResponse>> getContentType() {
        return ApiResponse.success(lookupService.getContentType(), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/account")
    public ApiResponse<List<LookupResponse>> getAccounts(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getAccounts(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/tag")
    public ApiResponse<List<LookupResponse>> getTags(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getTags(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/actor")
    public ApiResponse<List<LookupResponse>> getActors(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getActors(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/section")
    public ApiResponse<List<LookupResponse>> getSections(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getSections(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/movie")
    public ApiResponse<List<MongoLookupResponse>> getMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getMovies(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }


    @GetMapping("/clip")
    public ApiResponse<List<MongoLookupResponse>> getClips(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getClips(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }


    @GetMapping("/series")
    public ApiResponse<List<MongoLookupResponse>> getSeries(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getSeries(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }


    @GetMapping("/tv-program")
    public ApiResponse<List<MongoLookupResponse>> getTvPrograms(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getTvPrograms(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }


    @GetMapping("/channel")
    public ApiResponse<List<MongoLookupResponse>> getChannels(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getChannels(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }


    @GetMapping("/team")
    public ApiResponse<List<LookupResponse>> getTeams(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getTeams(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }


    @GetMapping("/champion")
    public ApiResponse<List<LookupResponse>> getChampions(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getChampions(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }


    @GetMapping("/kids-movie")
    public ApiResponse<List<MongoLookupResponse>> getKidsMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getKidsMovies(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }


    @GetMapping("/sport-movie")
    public ApiResponse<List<MongoLookupResponse>> getSportMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getSportMovies(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }


    @GetMapping("/kids-channel")
    public ApiResponse<List<MongoLookupResponse>> getKidsChannels(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getKidsChannels(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/sport-channel")
    public ApiResponse<List<MongoLookupResponse>> getSportChannels(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getSportChannels(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/kids-clip")
    public ApiResponse<List<MongoLookupResponse>> getKidsClips(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getKidsClips(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/sport-clip")
    public ApiResponse<List<MongoLookupResponse>> getSportClips(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getSportClips(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/kids-tv-program")
    public ApiResponse<List<MongoLookupResponse>> getKidsTvPrograms(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getKidsTvPrograms(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/sport-tv-program")
    public ApiResponse<List<MongoLookupResponse>> getSportTvPrograms(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getSportTvPrograms(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/kids-series")
    public ApiResponse<List<MongoLookupResponse>> getKidsSeries(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getKidsSeries(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/sport-series")
    public ApiResponse<List<MongoLookupResponse>> getSportSeries(
            @RequestParam(value = "name", required = false) String name,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return ApiResponse.success(
                lookupService.getSportSeries(name, lang),
                ApiResponseCode.SUCCESS_200
        );
    }


}