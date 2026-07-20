package com.rand.ishowApi.home.admin.domain.index;


import lombok.Getter;

@Getter
public enum HomeIndex {
    HOME("home");
    private final String indexName;
    HomeIndex(String indexName){
        this.indexName = indexName;
    }
}
