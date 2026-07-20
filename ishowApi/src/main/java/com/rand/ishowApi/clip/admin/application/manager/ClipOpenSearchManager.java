package com.rand.ishowApi.clip.admin.application.manager;

import com.rand.ishowApi.clip.admin.domain.entity.Clip;
import com.rand.ishowApi.clip.admin.domain.openSearch.model.ClipDocument;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class ClipOpenSearchManager {

    public ClipDocument createClipSectionDocument(Clip clip, Section section, String isTop) {
        ClipDocument doc = new ClipDocument();
        doc.setSectionId(section.getId());
        doc.setSectionTitleAr(section.getTitleAr());
        doc.setSectionTitleEn(section.getTitleEn());
        doc.setSectionOrder(section.getOrder());
        doc.setSectionActive(section.getActive());
        doc.setSectionPublish(section.getPublish());
        doc.setClipId(clip.getId());
        doc.setTitleEn(clip.getTitleEn());
        doc.setTitleAr(clip.getTitleAr());
        doc.setPoster(clip.getPoster());
        doc.setDuration(clip.getDuration());
        doc.setBadge(clip.getBadge());
        doc.setTags(clip.getTags());
        doc.setAccessType(clip.getAccessType());
        doc.setTranscodeMetaData(clip.getTranscodeMetaData());
        doc.setOriginalUploadMetadata(clip.getOriginalUploadMetadata());
        doc.setTranscodeStatus(clip.getTranscodeStatus());
        doc.setIsPublish(clip.getIsPublish());
        doc.setHasRight(clip.getHasRight());
        doc.setActive(clip.getActive());
        doc.setIsKids(clip.getIsKids());
        doc.setIsSports(clip.getIsSports());
        doc.setIsTop(BooleanConverter.getActiveBoolean(isTop));
        doc.setCreateDate( clip.getCreatedAt() != null ? Timestamp.valueOf(clip.getCreatedAt() ): Timestamp.valueOf(LocalDateTime.now()));
        doc.setPublishDate(clip.getPublishDate());
        return doc;
    }

    public void updateClipSectionDocument(ClipDocument doc, Clip clip) {
        doc.setTitleEn(clip.getTitleEn());
        doc.setTitleAr(clip.getTitleAr());
        doc.setPoster(clip.getPoster());
        doc.setDuration(clip.getDuration());
        doc.setBadge(clip.getBadge());
        doc.setTags(clip.getTags());
        doc.setAccessType(clip.getAccessType());
        doc.setTranscodeMetaData(clip.getTranscodeMetaData());
        doc.setOriginalUploadMetadata(clip.getOriginalUploadMetadata());
        doc.setTranscodeStatus(clip.getTranscodeStatus());
        doc.setIsPublish(clip.getIsPublish());
        doc.setHasRight(clip.getHasRight());
        doc.setActive(clip.getActive());
        doc.setIsKids(clip.getIsKids());
        doc.setIsSports(clip.getIsSports());
        doc.setCreateDate(
                clip.getCreatedAt() != null
                        ? Timestamp.valueOf(clip.getCreatedAt())
                        : Timestamp.valueOf(LocalDateTime.now())
        );

        doc.setPublishDate(clip.getPublishDate() );
    }

    public void setIsTop(ClipDocument doc, String isTop) {
        doc.setIsTop(BooleanConverter.getActiveBoolean(isTop) );
    }
}
