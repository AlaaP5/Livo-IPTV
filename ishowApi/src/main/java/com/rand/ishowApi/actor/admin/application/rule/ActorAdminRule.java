package com.rand.ishowApi.actor.admin.application.rule;

import com.rand.ishowApi.actor.admin.api.request.ActorAdminRequest;
import com.rand.ishowApi.actor.domain.repository.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActorAdminRule {

    private final ActorRepository actorRepository;

    public void validateActorNameExists(ActorAdminRequest request) {


    }

//    public Actor validateActorExists(Long actorId) {
//        return actorRepository.findById(actorId)
//                .orElseThrow(() -> new CustomException(ApiResponseCode.ACTOR_NOT_EXISTS, null));
//    }
}
