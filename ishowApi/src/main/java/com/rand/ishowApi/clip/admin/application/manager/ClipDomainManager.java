package com.rand.ishowApi.clip.admin.application.manager;

import com.rand.ishowApi.clip.admin.api.request.ClipAdminRequest;
import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.TranscodeStatus;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
@Component
public class ClipDomainManager {

    public Clip create(ClipAdminRequest request, List<TagMeta> tags) {
        return Clip.builder()
                .titleEn(request.getTitleEn())
                .titleAr(request.getTitleAr())
                .descriptionEn(request.getDescriptionEn())
                .descriptionAr(request.getDescriptionAr())
                .badge(request.getBadge())
                .tags(tags)
                .accessType(request.getAccessType())
                .duration(request.getDuration())
                .hasRight(BooleanConverter.getActiveBoolean(request.getHasRight()))
                .transcodeStatus(TranscodeStatus.PENDING)
                .isPublish(false)
                .active(BooleanConverter.getActiveBoolean(request.getActive()))
                .isKids(BooleanConverter.getActiveBoolean(request.getIsKids()))
                .isSports(BooleanConverter.getActiveBoolean(request.getIsSports()))
                .build();
    }

    public void addClipFiles(Clip clip, OriginalUploadMetadata file, OriginalUploadMetadata poster) {
        clip.setOriginalUploadMetadata(file);
        clip.setPoster(poster);
    }

    public void addClipTranscodeFile(Clip clip, TranscodeMetaData transcodeMetaData) {
        clip.setTranscodeMetaData(transcodeMetaData);
        clip.setTranscodeStatus(TranscodeStatus.COMPLETED);
    }

    public void publishClip(Clip clip) {
        clip.setIsPublish(true);
        //clip.setPublishDate(Timestamp.valueOf(LocalDateTime.now()));
        clip.setPublishDate(LocalDateTime.now().toString());
    }

    public void update(Clip clip,
                       ClipAdminRequest request,
                       List<TagMeta> tags,
                       OriginalUploadMetadata originalVideo,
                       OriginalUploadMetadata poster) {
        if (request.getTitleEn() != null) {
            clip.setTitleEn(request.getTitleEn());
        }
        if (request.getTitleAr() != null) {
            clip.setTitleAr(request.getTitleAr());
        }
        if (request.getDescriptionEn() != null) {
            clip.setDescriptionEn(request.getDescriptionEn());
        }
        if (request.getDescriptionAr() != null) {
            clip.setDescriptionAr(request.getDescriptionAr());
        }
        if (request.getBadge() != null) {
            clip.setBadge(request.getBadge());
        }
        if (request.getAccessType() != null) {
            clip.setAccessType(request.getAccessType());
        }
        if (request.getDuration() != null) {
            clip.setDuration(request.getDuration());
        }
        if (request.getHasRight() != null) {
            clip.setHasRight(BooleanConverter.getActiveBoolean(request.getHasRight()));
        }
        if (request.getActive() != null) {
            clip.setActive(BooleanConverter.getActiveBoolean(request.getActive()));
        }
        if (request.getIsKids() != null) {
            clip.setIsKids(BooleanConverter.getActiveBoolean(request.getIsKids()));
        }
        if (request.getIsSports() != null) {
            clip.setIsSports(BooleanConverter.getActiveBoolean(request.getIsSports()));
        }
        if (tags != null) {
            clip.setTags(tags);
        }
        if (originalVideo != null) {
            clip.setOriginalUploadMetadata(originalVideo);
        }
        if (poster != null) {
            clip.setPoster(poster);
        }
    }
}
