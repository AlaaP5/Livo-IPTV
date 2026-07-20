package com.rand.ishowApi.channel.admin.api.controller;

import com.rand.ishowApi.channel.admin.api.request.ChannelAdminRequest;
import com.rand.ishowApi.channel.admin.api.request.FilterChannelAdminRequest;
import com.rand.ishowApi.channel.admin.api.response.ChannelAdminFilterResponse;
import com.rand.ishowApi.channel.admin.api.response.ChannelAdminResponse;
import com.rand.ishowApi.channel.admin.api.response.ChannelAdminSectionResponse;
import com.rand.ishowApi.channel.admin.application.service.ChannelAdminService;
import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin/channel")
@RequiredArgsConstructor
public class ChannelAdminController {

    private final ChannelAdminService channelAdminService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ChannelAdminResponse> createChannel(@ModelAttribute ChannelAdminRequest request) {
        return ApiResponse.success(channelAdminService.addChannel(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ChannelAdminResponse> updateChannel(@ModelAttribute ChannelAdminRequest request) throws IOException {
        return ApiResponse.success(channelAdminService.updateChannel(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{channelId}")
    public ApiResponse<Channel> findChannelById(@PathVariable String channelId) {
        return ApiResponse.success(channelAdminService.findChannelById(channelId), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{id}/status")
    public ApiResponse<ChannelAdminResponse> changeChannelStatus(@PathVariable String id, @RequestParam String active) {
        return ApiResponse.success(channelAdminService.activateChannel(id, active), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/publish/{channelId}")
    public ApiResponse<Void> publishChannel(@PathVariable String channelId) throws java.io.IOException {
        channelAdminService.publishChannel(channelId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/list")
    public ApiResponse<List<ChannelAdminFilterResponse>> filterChannels(FilterChannelAdminRequest request) {

        Page<ChannelAdminFilterResponse> page = channelAdminService.filterChannels(request);

        return ApiResponse.success(
                page.getContent(),
                ApiResponseCode.SUCCESS_200,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }

    @GetMapping("/section/add")
    public ApiResponse<Void> addChannelToSection(@RequestParam String channelId,
                                                 @RequestParam Long sectionId,
                                                 @RequestParam(required = false) String isTop) throws java.io.IOException {

        channelAdminService.addChannelToSection(channelId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/section/remove")
    public ApiResponse<Void> removeChannelFromSection(@RequestParam Long sectionId,
                                                      @RequestParam String channelId) {

        channelAdminService.removeChannelFromSection(sectionId, channelId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/section/list")
    public ApiResponse<List<ChannelAdminSectionResponse>> getChannelsSection(@RequestParam Long sectionId,
                                                                             @RequestParam(required = false) String isTop,
                                                                             @RequestParam(defaultValue = "1") int page,
                                                                             @RequestParam(defaultValue = "10") int size) throws java.io.IOException {

        List<ChannelAdminSectionResponse> result = channelAdminService.getChannelsSection(sectionId, isTop, page, size);

        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                page,
                size
        );
    }

    @PutMapping("/section/is-top")
    public ApiResponse<Void> updateChannelIsTop(@RequestParam Long sectionId,
                                                @RequestParam String channelId,
                                                @RequestParam String isTop) throws java.io.IOException {
        channelAdminService.updateChannelIsTop(sectionId, channelId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }
}
