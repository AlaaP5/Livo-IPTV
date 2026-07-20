package com.rand.ishowApi.series.mobile.mapper;



import com.rand.ishowApi.openSearch.mapper.SectionContentMapper;
import com.rand.ishowApi.series.admin.domain.openSearch.model.SeriesDocument;
import com.rand.ishowApi.series.mobile.api.response.SeriesSectionResponse;
import com.rand.ishowApi.utils.LocalizedText;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class SeriesDocMapper implements SectionContentMapper<SeriesDocument, SeriesSectionResponse> {

    @Override
    public SeriesSectionResponse map(SeriesDocument doc, String lang) {
        SeriesSectionResponse response = new SeriesSectionResponse();

        response.setSeriesId(doc.getSeriesId());
        response.setTitle(LocalizedText.getName(doc.getTitleEn(), doc.getTitleAr(), lang));
        response.setBadge(doc.getBadge());
        response.setRating(doc.getRating());
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
        }else {
            response.setTags(Collections.emptyList());
        }

            return response;


        }
}

