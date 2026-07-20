package com.rand.ishowApi.clip.admin.mapper;

import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.clip.admin.domain.openSearch.model.ClipSearchDocument;
import org.springframework.stereotype.Component;

@Component
public class ClipSearchMapper {

    public ClipSearchDocument toDoc(Clip clip) {
        if (clip == null) return null;

        ClipSearchDocument doc = new ClipSearchDocument();

        doc.setId(clip.getId());
        doc.setTitleEn(clip.getTitleEn());
        doc.setTitleAr(clip.getTitleAr());
        doc.setDescriptionEn(clip.getDescriptionEn());
        doc.setDescriptionAr(clip.getDescriptionAr());

        doc.setBadge(clip.getBadge());
        doc.setPoster(clip.getPoster());

        doc.setTags(clip.getTags());
        doc.setAccessType(clip.getAccessType());
        doc.setDuration(clip.getDuration());

        doc.setTranscodeMetaData(clip.getTranscodeMetaData());
        doc.setOriginalUploadMetadata(clip.getOriginalUploadMetadata());

        doc.setTranscodeStatus(clip.getTranscodeStatus());
        doc.setIsPublish(clip.getIsPublish());
        doc.setHasRight(clip.getHasRight());
        doc.setActive(clip.getActive());
        doc.setIsKids(clip.getIsKids());
        doc.setIsSports(clip.getIsSports());

        doc.setPublishDate(clip.getPublishDate());

        return doc;
    }
}

