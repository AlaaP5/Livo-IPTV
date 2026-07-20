package com.rand.ishowApi.sport.admin.application.manager;

import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.sport.admin.api.request.TeamAdminRequest;
import com.rand.ishowApi.sport.admin.domain.entity.Team;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

@Component
public class TeamDomainManager {

    public Team createTeam(TeamAdminRequest request) {

        Team team = new Team();

        team.setActive(BooleanConverter.getActiveBoolean(request.active()));
        team.setNameAr(request.nameAr());
        team.setNameEn(request.nameEn());
        team.setFullLogoPath("");
        team.setGeneratedLogoPath("");

        return team;
    }

    public void updateTeam(TeamAdminRequest request, Team team, OriginalUploadMetadata logo) {

        team.setActive(BooleanConverter.getActiveBoolean(request.active()));
        team.setNameEn(request.nameEn());
        team.setNameAr(request.nameAr());
        if (logo != null) {
            team.setFullLogoPath(logo.getFullPath());
            team.setGeneratedLogoPath(logo.getGeneratedPath());
        }
    }

    public void addTeamLogo(Team team, OriginalUploadMetadata logo) {
        team.setGeneratedLogoPath(logo.getGeneratedPath());
        team.setFullLogoPath(logo.getFullPath());
    }

    public void activateTeam(Team team, String active) {

        team.setActive(BooleanConverter.getActiveBoolean(active));
    }
}

