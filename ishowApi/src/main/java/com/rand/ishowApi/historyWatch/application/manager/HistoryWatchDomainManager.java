package com.rand.ishowApi.historyWatch.application.manager;

import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.historyWatch.domain.entity.HistoryWatch;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class HistoryWatchDomainManager {

    public HistoryWatch create(Long accountId, String contentId, ContentType contentType) {
        return HistoryWatch.builder()
                .accountId(accountId)
                .contentId(contentId)
                .contentType(contentType)
                .active(true)
                .build();
    }

    public HistoryWatch reactivate(HistoryWatch historyWatch) {
        historyWatch.setActive(true);
        return historyWatch;
    }

    public HistoryWatch deactivate(HistoryWatch historyWatch) {
        historyWatch.setActive(false);
        return historyWatch;
    }
}

