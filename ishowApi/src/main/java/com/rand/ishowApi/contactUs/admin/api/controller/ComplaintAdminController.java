package com.rand.ishowApi.contactUs.admin.api.controller;

import com.rand.ishowApi.contactUs.admin.api.request.ComplaintFilterAdminRequest;
import com.rand.ishowApi.contactUs.admin.api.response.ComplaintAdminResponse;
import com.rand.ishowApi.contactUs.admin.application.service.ComplaintAdminService;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/complaint")
@RequiredArgsConstructor
public class ComplaintAdminController {

    private final ComplaintAdminService complaintAdminService;

    @GetMapping("/filter")
    public ApiResponse<List<ComplaintAdminResponse>> filterComplaint(ComplaintFilterAdminRequest request) {

        Page<ComplaintAdminResponse> page = complaintAdminService.filterComplaint(request);

        return ApiResponse.success(
                page.getContent(),
                ApiResponseCode.SUCCESS_200,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }

    @GetMapping("/{id}/status")
    public ApiResponse<ComplaintAdminResponse> changeComplaintStatus(@PathVariable Long id, @RequestParam String status) {
        return ApiResponse.success(complaintAdminService.changeComplaintStatus(id, status), ApiResponseCode.SUCCESS_200);
    }
}
