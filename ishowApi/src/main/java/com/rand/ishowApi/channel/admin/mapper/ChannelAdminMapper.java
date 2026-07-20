package com.rand.ishowApi.channel.admin.mapper;

import com.rand.ishowApi.channel.admin.api.response.ChannelAdminFilterResponse;
import com.rand.ishowApi.channel.admin.api.response.ChannelAdminResponse;
import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChannelAdminMapper {

    public ChannelAdminResponse toResponse (Channel channel){

        ChannelAdminResponse response = new ChannelAdminResponse();

        response.setId(channel.getId());
        response.setTitleEn(channel.getTitleEn());
        response.setTitleAr(channel.getTitleAr());
        response.setDescriptionEn(channel.getDescriptionEn());
        response.setDescriptionAr(channel.getDescriptionAr());
        response.setBadge(channel.getBadge());
        response.setTags(channel.getTags());
        response.setLogo(channel.getLogo());
        response.setHasRight(channel.getHasRight());
        response.setIsKids(channel.getIsKids());
        response.setActive(channel.getActive());
        response.setIsSports(channel.getIsSports());
        response.setIsPublish(channel.getIsPublish());

        return response;
    }

    public List<ChannelAdminFilterResponse> toFilterResponse(List<Channel> channels){
        return channels.stream().map(channel -> {
            ChannelAdminFilterResponse response = new ChannelAdminFilterResponse();

            response.setId(channel.getId());
            response.setTitleEn(channel.getTitleEn());
            response.setTitleAr(channel.getTitleAr());
            response.setDescriptionEn(channel.getDescriptionEn());
            response.setDescriptionAr(channel.getDescriptionAr());
            response.setBadge(channel.getBadge());
            response.setTags(channel.getTags());
            response.setLogo(channel.getLogo());
            response.setHasRight(channel.getHasRight());
            response.setIsKids(channel.getIsKids());
            response.setActive(channel.getActive());
            response.setIsSports(channel.getIsSports());
            response.setIsPublish(channel.getIsPublish());

            return response;
        }).collect(Collectors.toList());
    }
}
