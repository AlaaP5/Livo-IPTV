package com.rand.ishowApi.clip.admin.mapper;

import com.rand.ishowApi.clip.admin.api.response.ClipsAdminResponse;
import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class ClipAdminMapper {
    public List<ClipsAdminResponse> toFilterResponse(List<Clip> clips) {
        return clips.stream().map(this::toFilterResponse).collect(Collectors.toList());
    }
    public ClipsAdminResponse toFilterResponse(Clip clip) {
        if (clip == null) {
            return null;
        }
        ClipsAdminResponse response = new ClipsAdminResponse();
        response.setClipId(clip.getId());
        response.setTitleEn(clip.getTitleEn());
        response.setTitleAr(clip.getTitleAr());
        response.setPoster(clip.getPoster());
        response.setDuration(clip.getDuration());
        response.setBadge(clip.getBadge());
        response.setAccessType(clip.getAccessType());
        response.setTags(clip.getTags());
        response.setHasRight(clip.getHasRight());
        response.setIsKids(clip.getIsKids());
        response.setActive(clip.getActive());
        response.setIsSports(clip.getIsSports());
        response.setTranscodeStatus(clip.getTranscodeStatus());
        response.setIsPublish(clip.getIsPublish());
        return response;
    }
}
