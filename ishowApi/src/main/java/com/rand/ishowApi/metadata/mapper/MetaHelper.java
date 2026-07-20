package com.rand.ishowApi.metadata.mapper;


import com.rand.ishowApi.actor.domain.entity.Actor;
import com.rand.ishowApi.channel.admin.domain.entity.Channel;
import com.rand.ishowApi.metadata.*;
import com.rand.ishowApi.sport.admin.domain.entity.Champion;
import com.rand.ishowApi.sport.admin.domain.entity.Team;
import com.rand.ishowApi.tag.admin.domain.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class MetaHelper {

    public List<TagMeta> mapMetaTag(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }

        return tags.stream()
                .map(tag -> {
                    TagMeta meta = new TagMeta();

                    meta.setId(tag.getId());
                    meta.setTitleAr(tag.getTitleAr());
                    meta.setTitleEn(tag.getTitleEn());
                    meta.setActive(tag.isActive());
                    return meta;
                })
                .toList();
    }

    public List<ActorMeta> mapMetaActor(List<Actor> actors) {
        if (actors == null || actors.isEmpty()) {
            return Collections.emptyList();
        }

        return actors.stream()
                .map(actor -> {
                    ActorMeta meta = new ActorMeta();

                    meta.setId(actor.getId());
                    meta.setNameAr(actor.getNameAr());
                    meta.setNameEn(actor.getNameEn());

                    // Important: flatten poster
                    if (actor.getFullImagePath() != null) {
                        meta.setFullImagePath(actor.getFullImagePath());
                    }

                    if (actor.getGeneratedImagePath() != null) {
                        meta.setGeneratedImagePath(actor.getGeneratedImagePath());
                    }

                    return meta;
                })
                .toList();
    }


    // -------------------- NEW METHODS --------------------

    public TeamMeta createTeamMeta(Team team) {

        TeamMeta meta = new TeamMeta();
        meta.setId(team.getId());
        meta.setNameAr(team.getNameAr());
        meta.setNameEn(team.getNameEn());
        meta.setFullImagePath(team.getFullLogoPath());
        meta.setGeneratedImagePath(team.getGeneratedLogoPath());

        return meta;
    }

    public ChampionMeta createChampionMeta(Champion champion ) {


        ChampionMeta meta = new ChampionMeta();
        meta.setId(champion.getId());
        meta.setNameAr(champion.getNameAr());
        meta.setNameEn(champion.getNameEn());
        meta.setFullImagePath(champion.getFullLogoPath());
        meta.setGeneratedImagePath(champion.getGeneratedLogoPath());

        return meta;
    }

    public ChannelMeta createChannelMeta( Channel channel) {

        ChannelMeta cm = new ChannelMeta();
        cm.setId(channel.getId());
        cm.setTitleAr(channel.getTitleAr());
        cm.setTitleEn(channel.getTitleEn());
        cm.setLogo(channel.getLogo());

        return cm;
    }
}
