package com.rand.ishowApi.sport.admin.application.service;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.sport.admin.api.request.TeamAdminFilterRequest;
import com.rand.ishowApi.sport.admin.api.request.TeamAdminRequest;
import com.rand.ishowApi.sport.admin.api.response.TeamAdminResponse;
import com.rand.ishowApi.sport.admin.application.manager.TeamDomainManager;
import com.rand.ishowApi.sport.admin.domain.entity.Team;
import com.rand.ishowApi.sport.admin.domain.repository.TeamRepository;
import com.rand.ishowApi.sport.admin.domain.repository.TeamSpecification;
import com.rand.ishowApi.sport.admin.mapper.TeamAdminMapper;
import com.rand.ishowApi.upload.service.UploadServiceAsync;
import com.rand.ishowApi.utils.BooleanConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TeamAdminService {

    private final TeamDomainManager manager;
    private final TeamAdminMapper mapper;
    private final TeamRepository repository;
    private final UploadServiceAsync uploadServiceAsync;
    private final MessageSource messageSource;

    public TeamAdminResponse createTeam(TeamAdminRequest request) {

        boolean exists = repository.existsByNameEnAndNameArAndActive(
                request.nameEn(),
                request.nameAr(),
                BooleanConverter.getActiveBoolean(request.active())
        );

        if (exists) {
            throw new CustomException(ApiResponseCode.TEAM_NAME_EXISTS, null);
        }

        Team team = manager.createTeam(request);

        team = repository.save(team);

        OriginalUploadMetadata metadata =
                uploadServiceAsync
                        .uploadTeamLogoAsync(
                                request.imagePath(),
                                team.getId().toString()
                        )
                        .join();

        manager.addTeamLogo(team, metadata);

        Team savedTeam = repository.save(team);

        return mapper.toResponse(savedTeam);
    }

    public TeamAdminResponse updateTeam(TeamAdminRequest request) {

        Team team = repository.findById(request.id())
                .orElseThrow(() ->
                        new CustomException(ApiResponseCode.TEAM_NOT_EXISTS, null));

        OriginalUploadMetadata logoMetadata = null;

        if (request.imagePath() != null) {

            logoMetadata = uploadServiceAsync
                    .uploadTeamLogoAsync(
                            request.imagePath(),
                            team.getId().toString()
                    )
                    .join();

            if (team.getFullLogoPath() != null) {
                uploadServiceAsync.removeOldFileAsync(team.getFullLogoPath());
            }
        }

        manager.updateTeam(request, team, logoMetadata);

        Team savedTeam = repository.save(team);

        return mapper.toResponse(savedTeam);
    }

    public TeamAdminResponse activateTeam(Long id, String active) {

        Team team = repository.findById(id)
                .orElseThrow(() ->
                        new CustomException(ApiResponseCode.TEAM_NOT_EXISTS, null));

        manager.activateTeam(team, active);

        Team savedTeam = repository.save(team);

        return mapper.toResponse(savedTeam);
    }

    public Integer createTeamsFromZip(MultipartFile zip) {

        List<MultipartFile> images =
                uploadServiceAsync.extractImagesFromZip(zip).join();

        if (images == null || images.isEmpty()) {
            return 0;
        }

        List<Team> teamsToSave = new ArrayList<>();

        for (MultipartFile image : images) {

            if (image == null || image.isEmpty()) {
                continue;
            }

            String nameEn = resolveImageName(image.getOriginalFilename());

            if (nameEn == null || nameEn.isBlank()) {
                continue;
            }

            String nameAr = resolveArabicName(nameEn);

            boolean exists = repository.existsByNameEnAndNameArAndActive(
                    nameEn,
                    nameAr,
                    true
            );

            if (exists) {
                continue;
            }

            TeamAdminRequest request = new TeamAdminRequest(
                    null,
                    nameEn,
                    nameAr,
                    image,
                    "true"
            );

            Team team = manager.createTeam(request);

            team = repository.save(team);

            OriginalUploadMetadata metadata =
                    uploadServiceAsync
                            .uploadTeamLogoAsync(
                                    image,
                                    team.getId().toString()
                            )
                            .join();

            manager.addTeamLogo(team, metadata);

            teamsToSave.add(team);
        }

        if (teamsToSave.isEmpty()) {
            return 0;
        }

        return repository.saveAll(teamsToSave).size();
    }

    public Page<TeamAdminResponse> filterTeam(TeamAdminFilterRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        Specification<Team> specification = Specification.where(TeamSpecification.hasActive(BooleanConverter.getActiveBoolean(request.getActive())))
                .and(TeamSpecification.hasName(request.getSearch()));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);
    }

    private String resolveArabicName(String nameEn) {

        return messageSource.getMessage(
                "team." + nameEn,
                null,
                nameEn,
                Locale.forLanguageTag("ar")
        );
    }

    private String resolveImageName(String originalFilename) {

        if (originalFilename == null || originalFilename.isBlank()) {
            return null;
        }

        int dotIndex = originalFilename.lastIndexOf('.');

        String rawName = dotIndex > 0
                ? originalFilename.substring(0, dotIndex)
                : originalFilename;

        return rawName.trim()
                .replace(' ', '_')
                .replace('-', '_')
                .replaceAll("_+", "_");
    }
}