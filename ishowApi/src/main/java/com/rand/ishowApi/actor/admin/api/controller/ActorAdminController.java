package com.rand.ishowApi.actor.admin.api.controller;

import com.rand.ishowApi.actor.admin.api.request.ActorAdminFilterRequest;
import com.rand.ishowApi.actor.admin.api.request.ActorAdminRequest;
import com.rand.ishowApi.actor.admin.api.response.ActorAdminResponse;
import com.rand.ishowApi.actor.admin.application.service.ActorAdminService;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/actor")
//@PreAuthorize("hasAnyRole('CONTENT')")
@RequiredArgsConstructor
public class ActorAdminController {

    private final ActorAdminService actorAdminService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ActorAdminResponse> createEvent(@ModelAttribute ActorAdminRequest request) {
        return ApiResponse.success(actorAdminService.createActor(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ActorAdminResponse> updateActor(@ModelAttribute ActorAdminRequest request) {
        return ApiResponse.success(actorAdminService.updateActor(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{id}/status")
    public ApiResponse<ActorAdminResponse> changeActorStatus(@PathVariable Long id, @RequestParam String active) {
        return ApiResponse.success(actorAdminService.activateActor(id, active), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/list")
    public ApiResponse<List<ActorAdminResponse>> filterActor(ActorAdminFilterRequest request) {

        Page<ActorAdminResponse> page = actorAdminService.filterActor(request);

        return ApiResponse.success(
                page.getContent(),
                ApiResponseCode.SUCCESS_200,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }
}
