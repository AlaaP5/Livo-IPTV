package com.rand.ishowApi.sport.admin.mapper;

import com.rand.ishowApi.sport.admin.domain.entity.UpcomingMatch;
import com.rand.ishowApi.sport.admin.api.response.UpcomingMatchAdminResponse;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class UpcomingMatchAdminMapper {

    public UpcomingMatchAdminResponse toResponse(UpcomingMatch match){
        if (match == null) return null;

        String date = match.getMatchDate() != null ? match.getMatchDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;

        return new UpcomingMatchAdminResponse(
                match.getId(),
                match.getHomeTeam(),
                match.getAwayTeam(),
                match.getChampion(),
                match.getChannel(),
                date,
                match.getActive()
        );
    }

    public java.util.List<UpcomingMatchAdminResponse> toResponseList(List<UpcomingMatch> list){
        return list.stream().map(this::toResponse).toList();
    }
}

