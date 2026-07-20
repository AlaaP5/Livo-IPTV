package com.rand.ishowApi.channel.admin.application.manager;


import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.channel.admin.domain.openSearch.model.ChannelDocument;
import com.rand.ishowApi.section.domain.entity.Section;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class ChannelOpenSearchManager {

    public ChannelDocument createChannelSectionDocument(Channel channel, Section section, String isTop) {
        ChannelDocument doc = new ChannelDocument();
        doc.setSectionId(section.getId());
        doc.setSectionTitleAr(section.getTitleAr());
        doc.setSectionTitleEn(section.getTitleEn());
        doc.setSectionOrder(section.getOrder());
        doc.setSectionActive(section.getActive());
        doc.setSectionPublish(section.getPublish());
        doc.setChannelId(channel.getId());
        doc.setTitleEn(channel.getTitleEn());
        doc.setTitleAr(channel.getTitleAr());
        doc.setLogo(channel.getLogo());
        doc.setBadge(channel.getBadge());
        doc.setTags(channel.getTags());
        doc.setIsPublish(channel.getIsPublish());
        doc.setHasRight(channel.getHasRight());
        doc.setActive(channel.getActive());
        doc.setIsKids(channel.getIsKids());
        doc.setIsSports(channel.getIsSports());
        doc.setIsTop(BooleanConverter.getActiveBoolean(isTop));
        doc.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        return doc;
    }

    public void updateChannelSectionDocument(ChannelDocument doc, Channel channel) {
        doc.setTitleEn(channel.getTitleEn());
        doc.setTitleAr(channel.getTitleAr());
        doc.setLogo(channel.getLogo());
        doc.setBadge(channel.getBadge());
        doc.setTags(channel.getTags());
        doc.setIsPublish(channel.getIsPublish());
        doc.setHasRight(channel.getHasRight());
        doc.setActive(channel.getActive());
        doc.setIsKids(channel.getIsKids());
        doc.setIsSports(channel.getIsSports());
    }

    public void setIsTop(ChannelDocument doc, String isTop) {
        doc.setIsTop(BooleanConverter.getActiveBoolean(isTop));
    }

}
