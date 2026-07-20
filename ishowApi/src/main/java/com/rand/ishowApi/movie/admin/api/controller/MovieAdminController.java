package com.rand.ishowApi.movie.admin.api.controller;


import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.movie.admin.api.request.FilterMovieAdminRequest;
import com.rand.ishowApi.movie.admin.api.request.MovieAdminRequest;
import com.rand.ishowApi.movie.admin.api.response.MovieAdminFilterResponse;
import com.rand.ishowApi.movie.admin.api.response.MovieAdminSectionResponse;
import com.rand.ishowApi.movie.admin.application.service.MovieAdminService;
import com.rand.ishowApi.movie.admin.domain.entity.Movies;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin/movie/")
@RequiredArgsConstructor
public class MovieAdminController {

    private final MovieAdminService movieAdminService;


    @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ApiResponse<Movies> addMovie( @ModelAttribute MovieAdminRequest request) {
        return ApiResponse.success(movieAdminService.addMovie(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping(value = "update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Movies> updateMovie( @ModelAttribute MovieAdminRequest request) throws IOException {
        return ApiResponse.success(movieAdminService.updateMovie(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{movieId}")
    public ApiResponse<Movies> findMovieById(@PathVariable String movieId) {
        return ApiResponse.success(movieAdminService.findMovieById(movieId), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("publish/{movieId}")
    public ApiResponse<Movies> publishMovie(@PathVariable String movieId) throws IOException {
        return ApiResponse.success(movieAdminService.publishMovie(movieId), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("filter")
    public ApiResponse<List<MovieAdminFilterResponse>> filter(@RequestBody FilterMovieAdminRequest request) {
        Page<MovieAdminFilterResponse> pageResult = movieAdminService.filter(request);
        return ApiResponse.success(
                pageResult.getContent(),
                ApiResponseCode.SUCCESS_200,
                pageResult.getTotalElements(),
                pageResult.getNumber() +1,
                pageResult.getSize()
        );
    }
    @GetMapping("section/add")
    public ApiResponse<Void> addMovieToSection(@RequestParam String movieId,
                                               @RequestParam Long sectionId,
                                               @RequestParam(required = false) String isTop) throws IOException {

        movieAdminService.addMovieToSection(movieId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("section/remove")
    public ApiResponse<Void> removeMovieFromSection(@RequestParam Long sectionId,
                                                    @RequestParam String movieId) {

        movieAdminService.removeMovieFromSection(sectionId, movieId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("section/list")
    public ApiResponse<List<MovieAdminSectionResponse>> getMoviesSection(@RequestParam Long sectionId,
                                                                         @RequestParam(required = false) String isTop,
                                                                         @RequestParam(defaultValue = "1") int page,
                                                                         @RequestParam(defaultValue = "10") int size) throws IOException {

        List<MovieAdminSectionResponse> result = movieAdminService.getMoviesSection(sectionId, isTop, page, size);

        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                page,
                size
        );
    }
    @PutMapping("section/is-top")
    public ApiResponse<Void> updateMovieIsTop(@RequestParam Long sectionId,
                                              @RequestParam String movieId,
                                              @RequestParam String isTop) throws IOException {
        movieAdminService.updateMovieIsTop(sectionId, movieId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    //TODO remove
    @GetMapping("updateTranscode")
    public ApiResponse<Void> updateMovieTranscodeResult(@RequestParam String movieId) throws IOException {

        TranscodeMetaData metaData = new TranscodeMetaData();
        metaData.setMasterPlaylist("");
        metaData.setVariants(new ArrayList<>());

        movieAdminService.updateMovieTranscodeResult(movieId, metaData);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }
}
