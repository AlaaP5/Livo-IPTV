package com.rand.ishowApi.sport.admin.api.controller;

import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.sport.admin.api.request.TeamAdminRequest;
import com.rand.ishowApi.sport.admin.api.response.TeamAdminResponse;
import com.rand.ishowApi.sport.admin.api.request.TeamAdminFilterRequest;
import com.rand.ishowApi.sport.admin.application.service.TeamAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/team")
@RequiredArgsConstructor
public class TeamAdminController {

    private final TeamAdminService teamAdminService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TeamAdminResponse> create(@ModelAttribute TeamAdminRequest request) {
        return ApiResponse.success(teamAdminService.createTeam(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TeamAdminResponse> update(@ModelAttribute TeamAdminRequest request) {
        return ApiResponse.success(teamAdminService.updateTeam(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping(value = "/upload-zip", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Integer> bulkUploadZip(@RequestParam("zip") MultipartFile zip) {
        return ApiResponse.success(teamAdminService.createTeamsFromZip(zip), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{id}/status")
    public ApiResponse<TeamAdminResponse> changeStatus(@PathVariable Long id, @RequestParam String active) {
        return ApiResponse.success(teamAdminService.activateTeam(id, active), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/list")
    public ApiResponse<List<TeamAdminResponse>> filter(TeamAdminFilterRequest request) {

        Page<TeamAdminResponse> page = teamAdminService.filterTeam(request);

        return ApiResponse.success(
                page.getContent(),
                ApiResponseCode.SUCCESS_200,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }
}

