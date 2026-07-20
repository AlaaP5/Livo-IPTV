package com.rand.ishowApi.sport.admin.application.service;

import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.sport.admin.api.request.ChampionAdminFilterRequest;
import com.rand.ishowApi.sport.admin.api.request.ChampionAdminRequest;
import com.rand.ishowApi.sport.admin.api.response.ChampionAdminResponse;
import com.rand.ishowApi.sport.admin.application.manager.ChampionDomainManager;
import com.rand.ishowApi.sport.admin.domain.entity.Champion;
import com.rand.ishowApi.sport.admin.domain.repository.ChampionRepository;
import com.rand.ishowApi.sport.admin.domain.repository.ChampionSpecification;
import com.rand.ishowApi.sport.admin.mapper.ChampionAdminMapper;
import com.rand.ishowApi.upload.service.UploadServiceAsync;
import com.rand.ishowApi.utils.BooleanConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ChampionAdminService {

    private final ChampionDomainManager manager;
    private final ChampionAdminMapper mapper;
    private final ChampionRepository repository;
    private final UploadServiceAsync uploadServiceAsync;

    public ChampionAdminResponse createChampion(ChampionAdminRequest request) {

        if (repository.existsByNameEnAndNameArAndActive(
                request.nameEn(),
                request.nameAr(),
                BooleanConverter.getActiveBoolean(request.active())
        )) {
            throw new CustomException(ApiResponseCode.CHAMPION_NAME_EXISTS, null);
        }

        Champion champion = manager.createChampion(request);
        repository.save(champion);
        CompletableFuture<OriginalUploadMetadata> logoFuture =
                uploadServiceAsync.uploadChampionLogoAsync(request.imagePath(), champion.getId().toString());

        manager.addChampionLogo(champion, logoFuture.join());
        Champion savedChampion = repository.save(champion);

        return mapper.toResponse(savedChampion);
    }


    public ChampionAdminResponse updateChampion(ChampionAdminRequest request) {

        Champion champion = repository.findById(request.id())
                .orElseThrow(() -> new CustomException(ApiResponseCode.CHAMPION_NOT_EXISTS, null));
        CompletableFuture<OriginalUploadMetadata> logoFuture = null;

        if (request.imagePath() != null) {
            logoFuture = uploadServiceAsync.uploadChampionLogoAsync(request.imagePath(), champion.getId().toString());
        }
        OriginalUploadMetadata logoMetadata = logoFuture != null ? logoFuture.join() : null;

        if (logoMetadata != null || (request.imagePath() != null)) {
            if (champion.getFullLogoPath() != null) {
                uploadServiceAsync.removeOldFileAsync(champion.getFullLogoPath());
            }
        }

        manager.updateChampion(request, champion, logoMetadata);
        repository.save(champion);
        return mapper.toResponse(champion);
    }


    public ChampionAdminResponse activateChampion(Long id, String active) {

        Champion champion = repository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.ACTOR_NOT_EXISTS, null));

        manager.activateChampion(champion, active);
        repository.save(champion);

        return mapper.toResponse(champion);
    }


    public Page<ChampionAdminResponse> filterChampion(ChampionAdminFilterRequest request){

        Pageable pageable = PageRequest.of(
                request.getPage() -1,
                request.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        Specification<Champion> specification = Specification
                .where(ChampionSpecification.hasActive(BooleanConverter.getActiveBoolean(request.getActive())))
                .and(ChampionSpecification.hasName(request.getSearch()));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);
    }
}