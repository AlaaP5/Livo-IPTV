package com.rand.ishowApi.clip.mobile.mapper;

import com.rand.ishowApi.clip.admin.domain.openSearch.model.ClipDocument;
import com.rand.ishowApi.clip.mobile.api.response.ClipSectionResponse;
import com.rand.ishowApi.openSearch.mapper.SectionContentMapper;
import com.rand.ishowApi.utils.DateUtils;
import com.rand.ishowApi.utils.DurationConverter;
import com.rand.ishowApi.utils.LocalizedText;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ClipDocMapper implements SectionContentMapper<ClipDocument, ClipSectionResponse> {

  @Override
  public   ClipSectionResponse map(ClipDocument doc, String lang){
        ClipSectionResponse clip =new ClipSectionResponse();
        clip.setClipId(doc.getClipId());
        clip.setDuration(DurationConverter.toTimeFormat(doc.getDuration()));
        if (doc.getPoster() != null) {
            clip.setPoster(doc.getPoster().getGeneratedPath());
        }
        if (doc.getTags() != null) {
            clip.setTags(
                    doc.getTags().stream()
                            .map(tag -> LocalizedText.getName(
                                    tag.getTitleEn(),
                                    tag.getTitleAr(),
                                    lang
                            ))
                            .collect(Collectors.toList())
            );
        }
        clip.setBadge(doc.getBadge());
        clip.setPublishDate(DateUtils.normalize(doc.getPublishDate()));
        clip.setTitle(LocalizedText.getName(doc.getTitleEn(), doc.getTitleAr(), lang));

        return clip;
    }

}
