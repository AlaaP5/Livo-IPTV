package com.rand.ishowApi.sport.admin.application.service;

import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SportAdminOpenSearchService {

    private final OpenSearchClient openSearchClient;


}
