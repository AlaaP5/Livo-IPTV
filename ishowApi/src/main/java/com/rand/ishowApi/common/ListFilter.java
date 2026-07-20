package com.rand.ishowApi.common;

import lombok.Data;

@Data
public class ListFilter {
    private String active;     // "1" | "0" | "true" | "false"
    private String search;
    private Integer page = 1;
    private Integer size = 10;
}
