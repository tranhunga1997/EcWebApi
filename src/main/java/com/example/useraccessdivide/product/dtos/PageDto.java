package com.example.useraccessdivide.product.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class PageDto<T> {
    private Collection<T> datas;
    private int totalPages;
    private long totalElements;
}
