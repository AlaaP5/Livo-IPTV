package com.rand.ishowApi.sport.admin.api.controller;

import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.sport.admin.api.request.ChampionAdminFilterRequest;
import com.rand.ishowApi.sport.admin.api.request.ChampionAdminRequest;
import com.rand.ishowApi.sport.admin.api.response.ChampionAdminResponse;
import com.rand.ishowApi.sport.admin.application.service.ChampionAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1/admin/champion")
@RequiredArgsConstructor
public class ChampionAdminController {

    private final ChampionAdminService championAdminService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ChampionAdminResponse> create(@ModelAttribute ChampionAdminRequest request) {
        return ApiResponse.success(championAdminService.createChampion(request), ApiResponseCode.SUCCESS_200);
    }


    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ChampionAdminResponse> update(@ModelAttribute ChampionAdminRequest request) {
        return ApiResponse.success(championAdminService.updateChampion(request), ApiResponseCode.SUCCESS_200);
    }


    @GetMapping("/{id}/status")
    public ApiResponse<ChampionAdminResponse> changeStatus(@PathVariable Long id, @RequestParam String active) {
        return ApiResponse.success(championAdminService.activateChampion(id, active), ApiResponseCode.SUCCESS_200);
    }


    @GetMapping("/list")
    public ApiResponse<List<ChampionAdminResponse>> filter(ChampionAdminFilterRequest request) {
        Page<ChampionAdminResponse> page = championAdminService.filterChampion(request);
        return ApiResponse.success(
                page.getContent(),
                ApiResponseCode.SUCCESS_200,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }
}