package com.example.useraccessdivide.product.forms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;

import com.example.useraccessdivide.product.anontation.FileRequired;

import lombok.Data;

@Data
public class ProductForm {
	@NotBlank(message = "Chưa nhập tên sản phẩm.")
    private String name;
	@NotBlank(message = "Chưa nhập giá sản phẩm.")
	@Pattern(regexp = "^\\d*$", message = "Giá sản phẩm chỉ được nhập số.")
    private String price;
    private String shortDescription;
    private String description;
    @NotBlank(message = "Chưa nhập category.")
    private String categoryId;
    @NotBlank(message = "Chưa nhập brand.")
    private String brandId;
    @NotBlank(message = "Chưa nhập ngày bắt đầu bán")
    private String startDatetime;
    private String endDatetime;
    @FileRequired(message = "Chưa chọn hình ảnh.")
    private MultipartFile file;
}
