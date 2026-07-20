package com.rand.ishowApi.channel.admin.mapper;

import com.rand.ishowApi.channel.admin.api.response.ChannelEpgAdminResponse;
import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.lookup.api.response.MongoLookupResponse;
import com.rand.ishowApi.channel.admin.domain.entity.ChannelEpg;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChannelEpgAdminMapper {

    public ChannelEpgAdminResponse toResponse(ChannelEpg e, Channel channel) {
        ChannelEpgAdminResponse r = new ChannelEpgAdminResponse();
        r.setId(e.getId());
        // map channelId -> MongoLookupResponse (only id populated)
        MongoLookupResponse ch = new MongoLookupResponse();
        ch.setId(channel.getId());
        ch.setName(channel.getTitleEn());
        r.setChannel(ch);

        r.setStartDate(e.getStartDate());
        r.setEndDate(e.getEndDate());
        r.setTitleEn(e.getTitleEn());
        r.setTitleAr(e.getTitleAr());
        r.setActive(e.getActive());
        return r;
    }

    public List<ChannelEpgAdminResponse> toFilterResponse(List<ChannelEpg> list, Channel channel) {
        return list.stream().map(e -> {
            ChannelEpgAdminResponse r = new ChannelEpgAdminResponse();
            r.setId(e.getId());

            MongoLookupResponse ch = new MongoLookupResponse();
            ch.setId(channel.getId());
            ch.setName(channel.getTitleEn());
            r.setChannel(ch);

            r.setStartDate(e.getStartDate());
            r.setEndDate(e.getEndDate());
            r.setTitleEn(e.getTitleEn());
            r.setTitleAr(e.getTitleAr());
            r.setActive(e.getActive());
            return r;
        }).collect(Collectors.toList());
    }
}

