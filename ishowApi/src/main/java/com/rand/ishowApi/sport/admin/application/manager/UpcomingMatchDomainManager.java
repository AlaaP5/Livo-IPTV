package com.rand.ishowApi.sport.admin.application.manager;

import com.rand.ishowApi.metadata.ChannelMeta;
import com.rand.ishowApi.metadata.ChampionMeta;
import com.rand.ishowApi.metadata.TeamMeta;
import com.rand.ishowApi.sport.admin.api.request.UpcomingMatchAdminRequest;
import com.rand.ishowApi.sport.admin.domain.entity.UpcomingMatch;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UpcomingMatchDomainManager {


    public UpcomingMatch create(UpcomingMatchAdminRequest request, TeamMeta homeTeam, TeamMeta awayTeam,
                                ChampionMeta champion, ChannelMeta channel) {
        UpcomingMatch match = new UpcomingMatch();

        if (request.matchDate() != null && !request.matchDate().isBlank()) {
            match.setMatchDate(LocalDateTime.parse(request.matchDate()));
        }

        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setChampion(champion);
        match.setChannel(channel);
        match.setActive(BooleanConverter.getActiveBoolean(request.active()));

        return match;
    }

    public void update(UpcomingMatchAdminRequest request, TeamMeta homeTeam, TeamMeta awayTeam,
                       ChampionMeta champion, ChannelMeta channel, UpcomingMatch match) {
        match.setActive(BooleanConverter.getActiveBoolean(request.active()));
        if (request.matchDate() != null && !request.matchDate().isBlank()) {
            match.setMatchDate(LocalDateTime.parse(request.matchDate()));
        }

        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setChampion(champion);
        match.setChannel(channel);
    }

    public void activate(UpcomingMatch match, String active) {
        match.setActive(BooleanConverter.getActiveBoolean(active));
    }
}

