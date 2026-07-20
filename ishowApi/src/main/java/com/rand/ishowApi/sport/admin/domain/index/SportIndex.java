package com.rand.ishowApi.sport.admin.domain.index;

import lombok.Getter;

@Getter
public enum SportIndex {

    SPORT("sport");

    private final String indexName;

    SportIndex(String indexName){
        this.indexName = indexName;
    }
}
