package com.rand.ishowApi.channel.admin.application.manager;

import com.rand.ishowApi.channel.admin.api.request.ChannelAdminRequest;
import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.metadata.TagMeta;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChannelDomainManager {

    public Channel create(ChannelAdminRequest req, List<TagMeta> tags) {
        return Channel.builder()
                .titleEn(req.getTitleEn())
                .titleAr(req.getTitleAr())
                .descriptionEn(req.getDescriptionEn())
                .descriptionAr(req.getDescriptionAr())
                .badge(req.getBadge())
                .tags(tags)
                .hasRight(BooleanConverter.getActiveBoolean(req.getHasRight()))
                .isKids(BooleanConverter.getActiveBoolean(req.getIsKids()))
                .isSports(BooleanConverter.getActiveBoolean(req.getIsSports()))
                .isPublish(false)
                .active(BooleanConverter.getActiveBoolean(req.getActive()))
                .build();

    }

    public void addChannelLogo(Channel channel, OriginalUploadMetadata logo) {
        channel.setLogo(logo);
    }

    public void setActive(Channel channel, String active) {
        channel.setActive(BooleanConverter.getActiveBoolean(active));
    }

    public void publishChannel(Channel channel) {
        channel.setIsPublish(true);
    }

    public void update(Channel channel, ChannelAdminRequest req, List<TagMeta> tags, OriginalUploadMetadata logo) {

        if (req.getTitleEn() != null) {
            channel.setTitleEn(req.getTitleEn());
        }

        if (req.getTitleAr() != null) {
            channel.setTitleAr(req.getTitleAr());
        }

        if (req.getDescriptionEn() != null) {
            channel.setDescriptionEn(req.getDescriptionEn());
        }

        if (req.getDescriptionAr() != null) {
            channel.setDescriptionAr(req.getDescriptionAr());
        }

        if (req.getBadge() != null) {
            channel.setBadge(req.getBadge());
        }

        if (tags != null) {
            channel.setTags(tags);
        }

        if (req.getHasRight() != null) {
            channel.setHasRight(BooleanConverter.getActiveBoolean(req.getHasRight()));
        }

        if (req.getIsKids() != null) {
            channel.setIsKids(BooleanConverter.getActiveBoolean(req.getIsKids()));
        }

        if (req.getIsSports() != null) {
            channel.setIsSports(BooleanConverter.getActiveBoolean(req.getIsSports()));
        }

        if (req.getActive() != null) {
            channel.setActive(BooleanConverter.getActiveBoolean(req.getActive()));
        }

        if (logo != null) {
            channel.setLogo(logo);
        }
    }
}
