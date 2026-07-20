package com.rand.ishowApi.sport.admin.api.response;

import com.rand.ishowApi.metadata.ChampionMeta;
import com.rand.ishowApi.metadata.ChannelMeta;
import com.rand.ishowApi.metadata.TeamMeta;

public record UpcomingMatchAdminResponse(
        String id,
        TeamMeta homeTeam,
        TeamMeta awayTeam,
        ChampionMeta champion,
        ChannelMeta channel,
        String matchDate,
        Boolean active
){

}

