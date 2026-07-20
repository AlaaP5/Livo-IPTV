package com.rand.ishowApi.actor.admin.mapper;

import com.rand.ishowApi.actor.admin.api.response.ActorAdminResponse;
import com.rand.ishowApi.actor.domain.entity.Actor;
import org.springframework.stereotype.Component;

@Component
public class ActorAdminMapper {

    public ActorAdminResponse toResponse(Actor actor) {

        ActorAdminResponse response = new ActorAdminResponse();

        response.setId(actor.getId());
        response.setNameEn(actor.getNameEn());
        response.setNameAr(actor.getNameAr());
        response.setImagePath(actor.getGeneratedImagePath());
        response.setActive(actor.isActive());

        return response;
    }
}
