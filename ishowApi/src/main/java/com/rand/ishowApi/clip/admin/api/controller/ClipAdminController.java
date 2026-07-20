package com.rand.ishowApi.clip.admin.api.controller;

import com.rand.ishowApi.clip.admin.api.request.ClipAdminRequest;
import com.rand.ishowApi.clip.admin.api.request.FilterClipsAdminRequest;
import com.rand.ishowApi.clip.admin.api.response.ClipSectionAdminResponse;
import com.rand.ishowApi.clip.admin.api.response.ClipsAdminResponse;
import com.rand.ishowApi.clip.admin.application.service.ClipAdminService;
import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



@RestController
@RequestMapping("api/v1/admin/clip/")
@RequiredArgsConstructor
public class ClipAdminController {

    private final ClipAdminService clipAdminService;

    @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Clip> createClip(@ModelAttribute ClipAdminRequest request) {
        return ApiResponse.success(clipAdminService.createClip(request), ApiResponseCode.SUCCESS_200);
    }


    @PostMapping(value = "update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Clip> updateClip(@ModelAttribute ClipAdminRequest request) throws IOException {
        return ApiResponse.success(clipAdminService.updateClip(request), ApiResponseCode.SUCCESS_200);
    }


    @GetMapping("/{clipId}")
    public ApiResponse<Clip> findClipById(@PathVariable String clipId) {
        return ApiResponse.success(clipAdminService.findClipById(clipId), ApiResponseCode.SUCCESS_200);
    }


    @GetMapping("publish/{clipId}")
    public ApiResponse<Clip> publishClip(@PathVariable String clipId) throws IOException {
        return ApiResponse.success(clipAdminService.publishClip(clipId), ApiResponseCode.SUCCESS_200);
    }


    @PostMapping("filter")
    public ApiResponse<List<ClipsAdminResponse>> filterClips(@RequestBody FilterClipsAdminRequest request) {
        Page<ClipsAdminResponse> pageResult = clipAdminService.filterClips(request);
        return ApiResponse.success(
                pageResult.getContent(),
                ApiResponseCode.SUCCESS_200,
                pageResult.getTotalElements(),
                pageResult.getNumber() + 1,
                pageResult.getSize()
        );
    }

    @GetMapping("section/add")
    public ApiResponse<Void> addClipToSection(@RequestParam String clipId,
                                              @RequestParam Long sectionId,
                                              @RequestParam(required = false) String isTop) throws IOException {

        clipAdminService.addClipToSection(clipId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("section/remove")
    public ApiResponse<Void> removeClipFromSection(@RequestParam Long sectionId,
                                                   @RequestParam String clipId) {

        clipAdminService.removeClipFromSection(sectionId, clipId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("section/list")
    public ApiResponse<List<ClipSectionAdminResponse>> getClipsSection(@RequestParam Long sectionId,
                                                                       @RequestParam(required = false) String isTop,
                                                                       @RequestParam(defaultValue = "1") int page,
                                                                       @RequestParam(defaultValue = "10") int size) throws IOException {

        List<ClipSectionAdminResponse> result = clipAdminService.getClipsSection(sectionId, isTop, page, size);

        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                page,
                size
        );
    }

    @PutMapping("section/is-top")
    public ApiResponse<Void> updateClipIsTop(@RequestParam Long sectionId,
                                             @RequestParam String clipId,
                                             @RequestParam String isTop) throws IOException {
        clipAdminService.updateClipIsTop(sectionId, clipId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    //TODO remove
    @GetMapping("updateTranscode")
    public ApiResponse<Void> updateClipTranscodeResult(@RequestParam String clipId) throws IOException {

        TranscodeMetaData metaData = new TranscodeMetaData();
        metaData.setMasterPlaylist("");
        metaData.setVariants(new ArrayList<>());

        clipAdminService.updateClipTranscodeResult(clipId, metaData);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }
}
