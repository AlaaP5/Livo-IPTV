package com.rand.ishowApi.watchList.application.manager;

import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.watchList.domain.entity.WatchList;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class WatchListDomainManager {

    public WatchList create(Long accountId, String contentId, ContentType contentType) {
        return WatchList.builder()
                .accountId(accountId)
                .contentId(contentId)
                .contentType(contentType)
                .active(true)
                .build();
    }

    public WatchList reactivate(WatchList watchList) {
        watchList.setActive(true);
        return watchList;
    }

    public WatchList deactivate(WatchList watchList) {
        watchList.setActive(false);
        return watchList;
    }
}


