package com.rand.ishowApi.search.api.request;

import lombok.Getter;

@Getter
public enum SortRequest {

    asc("asc"),
    desc("desc");
    private final String value;
    SortRequest(String value) {
        this.value = value;
    }
}
