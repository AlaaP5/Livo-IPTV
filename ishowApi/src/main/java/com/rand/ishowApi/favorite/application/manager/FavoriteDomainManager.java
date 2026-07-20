package com.rand.ishowApi.favorite.application.manager;

import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.favorite.domain.entity.Favorite;
import org.springframework.stereotype.Component;

@Component
public class FavoriteDomainManager {

    public Favorite create(Long accountId, String contentId, ContentType contentType) {
        return Favorite.builder()
                .accountId(accountId)
                .contentId(contentId)
                .contentType(contentType)
                .active(true)
                .build();
    }

    public Favorite reactivate(Favorite favorite) {
        favorite.setActive(true);
        return favorite;
    }

    public Favorite deactivate(Favorite favorite) {
        favorite.setActive(false);
        return favorite;
    }
}


