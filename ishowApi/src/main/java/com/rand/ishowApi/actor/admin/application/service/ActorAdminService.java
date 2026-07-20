package com.rand.ishowApi.actor.admin.application.service;

import com.rand.ishowApi.actor.admin.api.request.ActorAdminFilterRequest;
import com.rand.ishowApi.actor.admin.api.request.ActorAdminRequest;
import com.rand.ishowApi.actor.admin.api.response.ActorAdminResponse;
import com.rand.ishowApi.actor.admin.application.manager.ActorDomainManager;
import com.rand.ishowApi.actor.domain.entity.Actor;
import com.rand.ishowApi.actor.domain.repository.ActorRepository;
import com.rand.ishowApi.actor.admin.mapper.ActorAdminMapper;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.storage.service.StorageService;
import com.rand.ishowApi.upload.service.UploadServiceAsync;
import com.rand.ishowApi.utils.BooleanConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import static com.rand.ishowApi.actor.domain.repository.ActorSpecification.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ActorAdminService {

    private final ActorDomainManager manager;
    private final ActorAdminMapper mapper;
//    private final ActorAdminRule rule;
    private final ActorRepository repository;
    private final UploadServiceAsync uploadServiceAsync;
    private final StorageService storageService;


    public ActorAdminResponse createActor(ActorAdminRequest request) {

        if (repository.existsByNameEnAndNameArAndActive(
                request.nameEn(),
                request.nameAr(),
                BooleanConverter.getActiveBoolean(request.active())
        )) {
            throw new CustomException(ApiResponseCode.ACTOR_NAME_EXISTS, null);
        }

        Actor actor = manager.createActor(request);

        repository.save(actor);

        CompletableFuture<OriginalUploadMetadata> posterFuture =
                uploadServiceAsync.uploadActorPosterAsync(request.imagePath(), actor.getId().toString());

        manager.addActorPoster(actor, posterFuture.join());

        Actor savedActor = repository.save(actor);

        repository.save(actor);

        return mapper.toResponse(savedActor);
    }

    public ActorAdminResponse updateActor(ActorAdminRequest request) {

        Actor actor = repository.findById(request.id())
                .orElseThrow(() -> new CustomException(ApiResponseCode.ACTOR_NOT_EXISTS, null));

        CompletableFuture<OriginalUploadMetadata> posterFuture = null;

        if (request.imagePath() != null) {
            posterFuture = uploadServiceAsync.uploadActorPosterAsync(request.imagePath(), actor.getId().toString());
        }

        OriginalUploadMetadata posterMetadata = posterFuture != null ? posterFuture.join() : null;

        if (posterMetadata != null || (request.imagePath() != null)) {
            if (actor.getGeneratedImagePath() != null) {
                uploadServiceAsync.removeOldFileAsync(actor.getGeneratedImagePath());
            }
        }

        manager.updateActor(request, actor, posterMetadata);

        repository.save(actor);

        return mapper.toResponse(actor);
    }

    public ActorAdminResponse activateActor(Long id, String active) {

        Actor actor = repository.findById(id)
                .orElseThrow(() -> new CustomException(ApiResponseCode.ACTOR_NOT_EXISTS, null));

        manager.activateActor(actor, active);

        repository.save(actor);

        return mapper.toResponse(actor);
    }

    public Page<ActorAdminResponse> filterActor(ActorAdminFilterRequest request){

        Pageable pageable = PageRequest.of(
                request.getPage() -1,
                request.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        Specification<Actor> specification = Specification
                .where(hasActive(BooleanConverter.getActiveBoolean(request.getActive())))
                .and(hasName(request.getSearch()));

        return repository.findAll(specification, pageable)
                .map(mapper::toResponse);
    }
}
