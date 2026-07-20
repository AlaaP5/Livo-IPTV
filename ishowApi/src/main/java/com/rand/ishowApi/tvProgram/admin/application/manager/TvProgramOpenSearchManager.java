package com.rand.ishowApi.tvProgram.admin.application.manager;

import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.model.TvProgramDocument;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class TvProgramOpenSearchManager {

    public void updateTvProgramSectionDocument(TvProgramDocument doc, TvProgram tvProgram) {
        doc.setTitleEn(tvProgram.getTitleEn());
        doc.setTitleAr(tvProgram.getTitleAr());
        doc.setDescriptionEn(tvProgram.getDescriptionEn());
        doc.setDescriptionAr(tvProgram.getDescriptionAr());
        doc.setPoster(tvProgram.getPoster());
        doc.setTags(tvProgram.getTags());
        doc.setBadge(tvProgram.getBadge());
        doc.setReleaseYear(tvProgram.getReleaseYear());
        doc.setIsPublish(tvProgram.getIsPublish());
        doc.setHasRight(tvProgram.getHasRight());
        doc.setLanguage(tvProgram.getLanguage());
        doc.setActive(tvProgram.getActive());
        doc.setIsKids(tvProgram.getIsKids());
        doc.setIsSports(tvProgram.getIsSports());
        doc.setSubtitleLanguages(tvProgram.getSubtitleLanguages());
        doc.setAudioLanguages(tvProgram.getAudioLanguages());
        doc.setSeasonCount(tvProgram.getSeasonCount());

    }

    public void setIsTop(TvProgramDocument doc, String isTop) {
        doc.setIsTop(BooleanConverter.getActiveBoolean(isTop));
    }

    public TvProgramDocument createTvProgramSectionDocument(TvProgram tvProgram, Section section, String isTop) {
        TvProgramDocument doc = new TvProgramDocument();
        doc.setSectionId(section.getId());
        doc.setSectionTitleAr(section.getTitleAr());
        doc.setSectionTitleEn(section.getTitleEn());
        doc.setSectionOrder(section.getOrder());
        doc.setSectionActive(section.getActive());
        doc.setSectionPublish(section.getPublish());
        doc.setTvProgramId(tvProgram.getId());
        doc.setTitleEn(tvProgram.getTitleEn());
        doc.setTitleAr(tvProgram.getTitleAr());
        doc.setDescriptionEn(tvProgram.getDescriptionEn());
        doc.setDescriptionAr(tvProgram.getDescriptionAr());
        doc.setPoster(tvProgram.getPoster());
        doc.setBadge(tvProgram.getBadge());
        doc.setTags(tvProgram.getTags());
        doc.setReleaseYear(tvProgram.getReleaseYear());
        doc.setIsPublish(tvProgram.getIsPublish());
        doc.setHasRight(tvProgram.getHasRight());
        doc.setLanguage(tvProgram.getLanguage());
        doc.setActive(tvProgram.getActive());
        doc.setIsKids(tvProgram.getIsKids());
        doc.setIsSports(tvProgram.getIsSports());
        doc.setSubtitleLanguages(tvProgram.getSubtitleLanguages());
        doc.setAudioLanguages(tvProgram.getAudioLanguages());
        doc.setSeasonCount(tvProgram.getSeasonCount());
        doc.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        doc.setIsTop(BooleanConverter.getActiveBoolean(isTop));
        return doc;
    }
}


