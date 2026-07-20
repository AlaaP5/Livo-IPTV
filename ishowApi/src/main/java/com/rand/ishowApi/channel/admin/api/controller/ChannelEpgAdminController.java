package com.rand.ishowApi.channel.admin.api.controller;

import com.rand.ishowApi.channel.admin.api.request.ChannelEpgAdminRequest;
import com.rand.ishowApi.channel.admin.api.request.FilterChannelEpgAdminRequest;
import com.rand.ishowApi.channel.admin.api.response.ChannelEpgAdminResponse;
import com.rand.ishowApi.channel.admin.application.service.ChannelEpgAdminService;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin/channel-epg")
@RequiredArgsConstructor
public class ChannelEpgAdminController {

    private final ChannelEpgAdminService channelEpgAdminService;

    /*@PostMapping("/create")
    public ApiResponse<ChannelEpgAdminResponse> create(@RequestBody ChannelEpgAdminRequest request) {
        return ApiResponse.success(channelEpgAdminService.add(request), ApiResponseCode.SUCCESS_200);
    }*/

    @PostMapping("/update")
    public ApiResponse<ChannelEpgAdminResponse> update(@RequestBody ChannelEpgAdminRequest request) {
        return ApiResponse.success(channelEpgAdminService.update(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{id}/status")
    public ApiResponse<ChannelEpgAdminResponse> changeStatus(@PathVariable String id, @RequestParam String active) {
        return ApiResponse.success(channelEpgAdminService.activate(id, active), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/list")
    public ApiResponse<List<ChannelEpgAdminResponse>> filter(FilterChannelEpgAdminRequest request) {

        Page<ChannelEpgAdminResponse> page = channelEpgAdminService.filter(request);

        return ApiResponse.success(
                page.getContent(),
                ApiResponseCode.SUCCESS_200,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }

    @PostMapping(value = "/upload/epg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<ChannelEpgAdminResponse>> uploadXml(
            @RequestPart("file") MultipartFile file,
            @RequestParam String channelId
    ) throws IOException {
        return ApiResponse.success(channelEpgAdminService.addAll(file, channelId), ApiResponseCode.SUCCESS_200);
    }
}

