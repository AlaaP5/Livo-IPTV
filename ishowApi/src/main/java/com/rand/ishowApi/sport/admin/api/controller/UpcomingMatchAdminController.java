package com.rand.ishowApi.sport.admin.api.controller;

import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.sport.admin.api.request.UpcomingMatchAdminFilterRequest;
import com.rand.ishowApi.sport.admin.api.request.UpcomingMatchAdminRequest;
import com.rand.ishowApi.sport.admin.api.response.UpcomingMatchAdminResponse;
import com.rand.ishowApi.sport.admin.application.service.UpcomingMatchAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/upcoming-match")
@RequiredArgsConstructor
public class UpcomingMatchAdminController {

    private final UpcomingMatchAdminService service;

    @PostMapping("/create")
    public ApiResponse<UpcomingMatchAdminResponse> create(@RequestBody UpcomingMatchAdminRequest request){
        return ApiResponse.success(service.create(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping( "/update")
    public ApiResponse<UpcomingMatchAdminResponse> update(@RequestBody UpcomingMatchAdminRequest request){
        return ApiResponse.success(service.update(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{id}/status")
    public ApiResponse<UpcomingMatchAdminResponse> changeStatus(@PathVariable String id, @RequestParam String active){
        return ApiResponse.success(service.changeStatus(id, active), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("/filter")
    public ApiResponse<List<UpcomingMatchAdminResponse>> filter(@RequestBody UpcomingMatchAdminFilterRequest request){
        Page<UpcomingMatchAdminResponse> page = service.filter(request);
        return ApiResponse.success(
                page.getContent(),
                ApiResponseCode.SUCCESS_200,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }
}

