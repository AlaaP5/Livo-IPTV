package com.rand.ishowApi.redis.mapper;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageWrapper<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;

    public static <T> PageWrapper<T> fromPage(Page<T> page) {
        return new PageWrapper<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.isFirst()
        );
    }

    public Page<T> toPage() {
        return new PageImpl<>(
                content,
                PageRequest.of(pageNumber, pageSize),
                totalElements
        );
    }
}