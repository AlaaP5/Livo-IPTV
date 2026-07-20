package com.rand.ishowApi.contactUs.mobile.api.controller;

import com.rand.ishowApi.contactUs.mobile.api.request.ComplaintMobileRequest;
import com.rand.ishowApi.contactUs.mobile.application.service.ComplaintMobileService;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/mobile/complaint")
@RequiredArgsConstructor
public class ComplaintMobileController {

    private final ComplaintMobileService complaintMobileService;

    @PostMapping("/create")
    public ApiResponse<Void> createComplaint(@Valid @RequestBody ComplaintMobileRequest complaintMobileRequest) throws IOException {

        complaintMobileService.createComplaint(complaintMobileRequest);

        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);

    }
}
