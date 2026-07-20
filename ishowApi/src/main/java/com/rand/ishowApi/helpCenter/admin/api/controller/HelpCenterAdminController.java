package com.rand.ishowApi.helpCenter.admin.api.controller;

import com.rand.ishowApi.helpCenter.admin.api.request.HelpCenterAdminRequest;
import com.rand.ishowApi.helpCenter.admin.api.response.HelpCenterAdminResponse;
import com.rand.ishowApi.helpCenter.admin.application.service.HelpCenterAdminService;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/admin/helpCenter")
@RequiredArgsConstructor
public class HelpCenterAdminController {

    private final HelpCenterAdminService helpCenterAdminService;

    @PostMapping("/update")
    public ApiResponse<HelpCenterAdminResponse> createHelpCenters(@Valid @RequestBody HelpCenterAdminRequest request) throws IOException {
        return ApiResponse.success(helpCenterAdminService.updateHelpCenter(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping
    public ApiResponse<HelpCenterAdminResponse> getHelpCenter() throws IOException {
        return ApiResponse.success(
                helpCenterAdminService.getHelpCenter(),
                ApiResponseCode.SUCCESS_200
        );
    }
}
