package com.example.useraccessdivide.product.forms;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductForm {
    private String thumbnailUrl;
    private String name;
    private int price;
    private String shortDescription;
    private String description;
    private long categoryId;
    private long brandId;
    private String startDatetime;
    private String endDatetime;
    private MultipartFile file;
}
