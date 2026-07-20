package com.rand.ishowApi.tag.admin.api.controller;


import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.tag.admin.api.request.TagAdminRequest;
import com.rand.ishowApi.tag.admin.api.request.TagFilterAdminRequest;
import com.rand.ishowApi.tag.admin.api.response.TagAdminResponse;
import com.rand.ishowApi.tag.admin.application.service.TagAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/tag")
@RequiredArgsConstructor
public class TagAdminController {

    private final TagAdminService service;

    @PostMapping("/create")
    public ApiResponse<TagAdminResponse> createTag(@Valid @RequestBody TagAdminRequest request) {
        TagAdminResponse response = service.createTag(request);
        return ApiResponse.success(response, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("/update")
    public ApiResponse<TagAdminResponse> updateTag(@Valid @RequestBody TagAdminRequest request) {
        return ApiResponse.success(service.updateTag(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{id}/status")
    public ApiResponse<TagAdminResponse> changeTagStatus(@PathVariable Long id, @RequestParam String active) {
        return ApiResponse.success(service.activateTag(id, active), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{id}/common")
    public ApiResponse<TagAdminResponse> changeTagCommon(@PathVariable Long id, @RequestParam String common) {
        return ApiResponse.success(service.activateCommon(id, common), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/list")
    public ApiResponse<List<TagAdminResponse>> filterTag(TagFilterAdminRequest request) {

        Page<TagAdminResponse> page = service.filterTag(request);

        return ApiResponse.success(
                page.getContent(),
                ApiResponseCode.SUCCESS_200,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }
}
