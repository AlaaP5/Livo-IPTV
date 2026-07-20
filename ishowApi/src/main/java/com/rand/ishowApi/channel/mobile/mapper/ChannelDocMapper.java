package com.rand.ishowApi.channel.mobile.mapper;

import com.rand.ishowApi.channel.admin.domain.openSearch.model.ChannelDocument;
import com.rand.ishowApi.channel.mobile.api.response.ChannelSectionResponse;
import com.rand.ishowApi.openSearch.mapper.SectionContentMapper;
import com.rand.ishowApi.utils.LocalizedText;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;


@Component
public class ChannelDocMapper implements SectionContentMapper<ChannelDocument, ChannelSectionResponse> {

    @Override
    public ChannelSectionResponse map(ChannelDocument doc, String lang) {
        ChannelSectionResponse response = new ChannelSectionResponse();
        response.setChannelId(doc.getChannelId());
        response.setTitle(LocalizedText.getName(doc.getTitleEn(), doc.getTitleAr(), lang));
        if (doc.getLogo() != null) {
            response.setLogo(doc.getLogo().getGeneratedPath());
        }
        response.setBadge(doc.getBadge());


        if (doc.getTags() != null) {
            response.setTags( doc.getTags().stream()
                    .map(tag -> LocalizedText.getName(
                            tag.getTitleEn(),
                            tag.getTitleAr(),
                            lang
                    ))
                    .collect(Collectors.toList()));

        } else {
            response.setTags(Collections.emptyList());
        }

        return response;
    }


}
