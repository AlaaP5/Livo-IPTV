package com.rand.ishowApi.openSearch.mapper;

public interface SectionContentMapper<D, R> {
    R map(D document, String lang);
}
