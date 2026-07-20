package com.rand.ishowApi.kids.admin.domain.index;


import lombok.Getter;

@Getter
public enum KidsIndex {
    KIDS("kids");

    private final String indexName;

    KidsIndex(String indexName){
        this.indexName = indexName;
    }
}

