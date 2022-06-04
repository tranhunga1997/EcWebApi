package com.example.useraccessdivide.product.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchForm {
    private String name;
    private int minPrice;
    private int maxPrice;
    private String categorySlug;
    private String brandSlug;
    private String priceOrder;
}
