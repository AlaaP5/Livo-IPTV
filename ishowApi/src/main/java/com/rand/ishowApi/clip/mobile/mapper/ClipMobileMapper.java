package com.rand.ishowApi.clip.mobile.mapper;

import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.clip.mobile.api.response.ClipsDetailsResponse;
import com.rand.ishowApi.lookup.api.response.LookupResponse;
import com.rand.ishowApi.security.context.DeviceContext;
import com.rand.ishowApi.utils.DateUtils;
import com.rand.ishowApi.utils.DurationConverter;
import com.rand.ishowApi.utils.LocalizedText;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClipMobileMapper {

    public ClipsDetailsResponse toClipDetails(Clip clip, List<Clip> relatedClips, boolean isLke,  boolean isWatchList) {
        String lang = DeviceContext.get().getLanguage().toString().toLowerCase();

        ClipsDetailsResponse response = new ClipsDetailsResponse();

        response.setClipsId(clip.getId());
        response.setTitle(LocalizedText.getName(clip.getTitleEn(), clip.getTitleAr(), lang));
        response.setDescription(LocalizedText.getName(clip.getDescriptionEn(), clip.getDescriptionAr(), lang));

        if (clip.getPoster() != null) {
            response.setPoster(clip.getPoster().getGeneratedPath());
        }

        if (clip.getTranscodeMetaData() != null) {
            //response.setStreamUrl(clip.getTranscodeMetaData().getMasterPlaylist());
            response.setStreamUrl("https://www.youtube.com/shorts/eOf-mUbPrUw");
        }

        response.setBadge(clip.getBadge());
        response.setPublishDate(DateUtils.normalize(clip.getPublishDate()) );
        response.setDuration(DurationConverter.toTimeFormat(clip.getDuration()));
        response.setAccessType(clip.getAccessType());

        // TAGS
        if (clip.getTags() != null) {
            response.setTags(
                    clip.getTags().stream().map(tag -> {
                        LookupResponse t = new LookupResponse();
                        t.setId(tag.getId());
                        t.setName(LocalizedText.getName(tag.getTitleEn(), tag.getTitleAr(), lang));
                        return t;
                    }).toList()
            );
        }

         List<ClipsDetailsResponse> related = mapRelatedClips(relatedClips, lang);
        response.setRelatedClips(related);
        response.setIsLike(isLke);
        response.setIsWatchList(isWatchList);

        return response;
    }

    private List<ClipsDetailsResponse> mapRelatedClips(List<Clip> clips, String lang) {
        return clips.stream().map(c -> {
            ClipsDetailsResponse r = new ClipsDetailsResponse();
            r.setClipsId(c.getId());
            r.setTitle(LocalizedText.getName(c.getTitleEn(), c.getTitleAr(), lang));
            r.setBadge(c.getBadge());
            if (c.getPoster() != null) {
                r.setPoster(c.getPoster().getGeneratedPath());
            }
            r.setDuration(DurationConverter.toTimeFormat(c.getDuration()));
            r.setAccessType(c.getAccessType());
            return r;
        }).collect(Collectors.toList());
    }
}
