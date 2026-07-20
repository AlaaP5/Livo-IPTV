package com.rand.ishowApi.channel.admin.mapper;

import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.channel.admin.domain.openSearch.model.ChannelSearchDocument;
import org.springframework.stereotype.Component;

@Component
public class ChannelSearchMapper {

    public ChannelSearchDocument toDoc(Channel channel) {
        if (channel == null) return null;

        ChannelSearchDocument doc = new ChannelSearchDocument();

        doc.setId(channel.getId());
        doc.setTitleEn(channel.getTitleEn());
        doc.setTitleAr(channel.getTitleAr());
        doc.setDescriptionEn(channel.getDescriptionEn());
        doc.setDescriptionAr(channel.getDescriptionAr());
        doc.setStreamUrl(channel.getStreamUrl());

        doc.setBadge(channel.getBadge());
        doc.setLogo(channel.getLogo());
        doc.setTags(channel.getTags());

        doc.setIsPublish(channel.getIsPublish());
        doc.setHasRight(channel.getHasRight());
        doc.setActive(channel.getActive());
        doc.setIsKids(channel.getIsKids());
        doc.setIsSports(channel.getIsSports());

        return doc;
    }
}

