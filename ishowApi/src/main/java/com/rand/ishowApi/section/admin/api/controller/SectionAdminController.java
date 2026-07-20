package com.rand.ishowApi.section.admin.api.controller;

import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.section.admin.api.request.SectionAdminRequest;
import com.rand.ishowApi.section.admin.api.request.SectionFilterAdminRequest;
import com.rand.ishowApi.section.admin.api.response.SectionAdminResponse;
import com.rand.ishowApi.section.admin.application.service.SectionAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/section")
@RequiredArgsConstructor
public class SectionAdminController {

    private final SectionAdminService service;

    @PostMapping("/create")
    public ApiResponse<SectionAdminResponse> createSection(@Valid @RequestBody SectionAdminRequest request) {
        return ApiResponse.success(service.createSection(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("/update")
    public ApiResponse<SectionAdminResponse> updateSection(@Valid @RequestBody SectionAdminRequest request) {
        return ApiResponse.success(service.updateSection(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{id}/status")
    public ApiResponse<SectionAdminResponse> changeSectionStatus(@PathVariable Long id, @RequestParam String active) {
        return ApiResponse.success(service.activateSection(id, active), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{id}/publish")
    public ApiResponse<SectionAdminResponse> changeSectionPublish(@PathVariable Long id, @RequestParam String publish) {
        return ApiResponse.success(service.publishSection(id, publish), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/list")
    public ApiResponse<List<SectionAdminResponse>> filterSection(SectionFilterAdminRequest request) {
        Page<SectionAdminResponse> page = service.filterSections(request);
        return ApiResponse.success(
                page.getContent(),
                ApiResponseCode.SUCCESS_200,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }
}

