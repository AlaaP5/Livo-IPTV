package com.rand.ishowApi.actor.admin.application.manager;

import com.rand.ishowApi.actor.admin.api.request.ActorAdminRequest;
import com.rand.ishowApi.actor.domain.entity.Actor;
import com.rand.ishowApi.metadata.video.OriginalUploadMetadata;
import com.rand.ishowApi.utils.BooleanConverter;
import org.springframework.stereotype.Component;

@Component
public class ActorDomainManager {

    public Actor createActor(ActorAdminRequest request) {

        Actor actor = new Actor();

        actor.setActive(BooleanConverter.getActiveBoolean(request.active()));
        actor.setNameAr(request.nameAr());
        actor.setNameEn(request.nameEn());
        actor.setFullImagePath("");
        actor.setGeneratedImagePath("");

        return actor;
    }

    public void updateActor(ActorAdminRequest request, Actor actor, OriginalUploadMetadata poster) {

        actor.setActive(BooleanConverter.getActiveBoolean(request.active()));
        actor.setNameEn(request.nameEn());
        actor.setNameAr(request.nameAr());
        if(poster != null) {
            actor.setFullImagePath(poster.getFullPath());
            actor.setGeneratedImagePath(poster.getGeneratedPath());
        }
    }

    public void addActorPoster(Actor actor, OriginalUploadMetadata poster) {
        actor.setGeneratedImagePath(poster.getGeneratedPath());
        actor.setFullImagePath(poster.getFullPath());
    }

    public void activateActor(Actor actor, String active) {

        actor.setActive(BooleanConverter.getActiveBoolean(active));
    }
}
