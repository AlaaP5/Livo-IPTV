package com.rand.ishowApi.openSearch.mapper;

public interface SectionNameResolver <D> {
    String resolve(D document, String lang);

}
