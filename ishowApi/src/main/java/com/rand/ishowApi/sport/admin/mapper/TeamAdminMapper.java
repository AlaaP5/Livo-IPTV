package com.rand.ishowApi.sport.admin.mapper;

import com.rand.ishowApi.sport.admin.api.response.TeamAdminResponse;
import com.rand.ishowApi.sport.admin.domain.entity.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamAdminMapper {

    public TeamAdminResponse toResponse(Team team) {

        TeamAdminResponse response = new TeamAdminResponse();

        response.setId(team.getId());
        response.setNameEn(team.getNameEn());
        response.setNameAr(team.getNameAr());
        response.setLogoPath(team.getGeneratedLogoPath());
        response.setActive(team.isActive());

        return response;
    }
}

