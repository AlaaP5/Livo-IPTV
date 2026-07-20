package com.rand.ishowApi.sport.mobile.api.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpcomingMatchResponse {

    private String id;
    private SportTeamResponse homeTeam;
    private SportTeamResponse awayTeam;
    private SportChampionResponse tournament;
    private SportChannelResponse channel;
    private LocalDateTime matchDate;
}
