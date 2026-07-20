package com.rand.ishowApi.watchList.mapper;

import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.clip.admin.domain.repository.ClipRepository;
import com.rand.ishowApi.clip.mobile.api.response.ClipSectionResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.utils.DateUtils;
import com.rand.ishowApi.utils.DurationConverter;
import com.rand.ishowApi.utils.LocalizedText;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClipWatchListAdapter implements WatchListContentAdapter<Clip, ClipSectionResponse> {
    private final ClipRepository clipRepository;

    @Override
    public ContentType contentType() {
        return ContentType.CLIPS;
    }

    @Override
    public String notFoundCode() {
        return ApiResponseCode.CLIP_NOT_FOUND;
    }

    @Override
    public Optional<Clip> findById(String contentId) {
        if (contentId == null) {
            return Optional.empty();
        }
        return clipRepository.findById(contentId);
    }

    @Override
    public Map<String, Clip> findAllByIds(Collection<String> contentIds) {
        if (contentIds == null || contentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Clip> clips = new ArrayList<>(clipRepository.findAllById(contentIds));
        return clips.stream().collect(Collectors.toMap(
                Clip::getId,
                Function.identity(),
                (left, right) -> left,
                LinkedHashMap::new
        ));
    }

    @Override
    public ClipSectionResponse toResponse(Clip clip, String lang) {
        ClipSectionResponse response = new ClipSectionResponse();
        response.setClipId(clip.getId());
        response.setTitle(LocalizedText.getName(clip.getTitleEn(), clip.getTitleAr(), lang));
        response.setBadge(clip.getBadge());
        if (clip.getPoster() != null) {
            response.setPoster(clip.getPoster().getGeneratedPath());
        }
        response.setPublishDate(DateUtils.normalize(clip.getPublishDate()));
        response.setDuration(clip.getDuration() == null ? null : DurationConverter.toTimeFormat(clip.getDuration()));
        response.setTags(clip.getTags() == null ? Collections.emptyList() : clip.getTags().stream()
                .map(tag -> LocalizedText.getName(tag.getTitleEn(), tag.getTitleAr(), lang))
                .toList());
        return response;
    }
}


