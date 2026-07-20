package com.rand.ishowApi.historyWatch.application.service;

import com.rand.ishowApi.account.domain.entity.Account;
import com.rand.ishowApi.account.mobile.application.service.AccountMobileService;
import com.rand.ishowApi.exception.model.CustomException;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.historyWatch.api.request.HistoryWatchRequest;
import com.rand.ishowApi.historyWatch.api.response.HistoryWatchItemResponse;
import com.rand.ishowApi.historyWatch.application.manager.HistoryWatchDomainManager;
import com.rand.ishowApi.historyWatch.domain.entity.HistoryWatch;
import com.rand.ishowApi.historyWatch.domain.repository.HistoryWatchRepository;
import com.rand.ishowApi.historyWatch.mapper.HistoryWatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class HistoryWatchService {
    private final HistoryWatchRepository historyWatchRepository;
    private final HistoryWatchMapper historyWatchMapper;
    private final HistoryWatchDomainManager historyWatchDomainManager;
    private final AccountMobileService accountService;

    @Transactional
    public void addToHistoryWatch(HistoryWatchRequest request) {
        Account account = accountService.getCurrentAccount();

        historyWatchMapper.findContent(request.getContentType(), request.getContentId())
                .orElseThrow(() -> new CustomException(historyWatchMapper.notFoundCode(request.getContentType()), null));

        HistoryWatch historyWatch = historyWatchRepository
                .findByAccountIdAndContentIdAndContentType(account.getId(), request.getContentId(), request.getContentType())
                .map(historyWatchDomainManager::reactivate)
                .orElseGet(() -> historyWatchDomainManager.create(account.getId(), request.getContentId(), request.getContentType()));

        historyWatchRepository.save(historyWatch);
    }


    public boolean isInHistoryWatch(String contentId, String contentType) {
        Account account = accountService.getCurrentAccount();
        return historyWatchRepository.countByAccountIdAndContentIdAndContentTypeAndActiveTrue(account.getId(), contentId, ContentType.fromValue(contentType)) > 0;
    }

    public Page<HistoryWatchItemResponse<?>> getHistoryWatch(String contentType, int page, int size) {

        ContentType type = ContentType.fromValue(contentType);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size);

        Account account = accountService.getCurrentAccount();

        Page<HistoryWatch> historyWatches =  historyWatchRepository.findAllByAccountIdAndContentTypeAndActiveTrueOrderByCreatedAtDesc(account.getId(), type, pageable);

        if (historyWatches == null || historyWatches.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        return new PageImpl<>(historyWatchMapper.map(historyWatches.getContent()), pageable, historyWatches.getTotalElements());
    }
}

