package com.rand.ishowApi.tvProgram.admin.mapper;

import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.model.TvProgramSearchDocument;
import org.springframework.stereotype.Component;

@Component
public class TvProgramSearchMapper {

    public TvProgramSearchDocument toDoc(TvProgram tvProgram) {
        if (tvProgram == null) return null;

        TvProgramSearchDocument doc = new TvProgramSearchDocument();

        doc.setId(tvProgram.getId());
        doc.setTitleEn(tvProgram.getTitleEn());
        doc.setTitleAr(tvProgram.getTitleAr());
        doc.setDescriptionEn(tvProgram.getDescriptionEn());
        doc.setDescriptionAr(tvProgram.getDescriptionAr());

        doc.setBadge(tvProgram.getBadge());
        doc.setPoster(tvProgram.getPoster());

        doc.setTags(tvProgram.getTags());

        doc.setReleaseYear(tvProgram.getReleaseYear());
        doc.setSeasonCount(tvProgram.getSeasonCount());

        doc.setSubtitleLanguages(tvProgram.getSubtitleLanguages());
        doc.setAudioLanguages(tvProgram.getAudioLanguages());

        doc.setIsPublish(tvProgram.getIsPublish());
        doc.setHasRight(tvProgram.getHasRight());
        doc.setLanguage(tvProgram.getLanguage());

        doc.setActive(tvProgram.getActive());
        doc.setIsKids(tvProgram.getIsKids());
        doc.setIsSports(tvProgram.getIsSports());

        return doc;
    }
}

