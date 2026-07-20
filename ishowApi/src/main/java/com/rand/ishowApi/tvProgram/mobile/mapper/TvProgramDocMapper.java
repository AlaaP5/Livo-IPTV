package com.rand.ishowApi.tvProgram.mobile.mapper;


import com.rand.ishowApi.openSearch.mapper.SectionContentMapper;
import com.rand.ishowApi.tvProgram.admin.domain.openSearch.model.TvProgramDocument;
import com.rand.ishowApi.tvProgram.mobile.api.response.TvProgramSectionResponse;
import com.rand.ishowApi.utils.LocalizedText;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class TvProgramDocMapper implements SectionContentMapper<TvProgramDocument, TvProgramSectionResponse> {
    @Override
    public TvProgramSectionResponse map(TvProgramDocument doc, String lang) {
        TvProgramSectionResponse response = new TvProgramSectionResponse();

        response.setTvProgramId(doc.getTvProgramId());
        response.setTitle(LocalizedText.getName(doc.getTitleEn(), doc.getTitleAr(), lang));
        response.setBadge(doc.getBadge());
        response.setReleaseYear(doc.getReleaseYear());
        response.setSeasonCount(doc.getSeasonCount());
        if (doc.getPoster() != null) {
            response.setPoster(doc.getPoster().getGeneratedPath());
        }
        if (doc.getTags() != null) {
            response.setTags(
                    doc.getTags().stream()
                            .map(tag -> LocalizedText.getName(
                                    tag.getTitleEn(),
                                    tag.getTitleAr(),
                                    lang
                            ))
                            .collect(Collectors.toList())
            );
        } else {
            response.setTags(Collections.emptyList());
        }
        return response;
    }
}
