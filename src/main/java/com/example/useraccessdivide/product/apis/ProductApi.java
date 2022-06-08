package com.example.useraccessdivide.product.apis;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.useraccessdivide.common.Pagingation;
import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.product.entities.ProductEntity;
import com.example.useraccessdivide.product.exceptions.ImageNotExtensionException;
import com.example.useraccessdivide.product.forms.ProductForm;
import com.example.useraccessdivide.product.forms.ProductSearchForm;
import com.example.useraccessdivide.product.services.BrandService;
import com.example.useraccessdivide.product.services.CategoryService;
import com.example.useraccessdivide.product.services.ProductService;
import com.example.useraccessdivide.product.specifications.ProductSpecification;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/product")
@Api(value = "Product api", tags = {"api sản phẩm"})
public class ProductApi {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductSpecification productSpecification;
    
    @ApiOperation(value = "Xem hình ảnh sản phẩm")
    @GetMapping(path = "/image/{fileName}", produces = {MediaType.IMAGE_GIF_VALUE,MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_PNG_VALUE})
    ResponseEntity viewImage(@PathVariable String fileName) throws IOException {
        return ResponseEntity.ok(productService.getByteImage(productService.getPathImage(fileName)));
    }

    @ApiOperation(value = "Xem và tìm kiếm sản phẩm")
    @GetMapping
    ResponseEntity<Pagingation<ProductEntity>> productView(ProductSearchForm form, @RequestParam(name = "page", defaultValue = "1") int currentPage){
    	form.setPriceOrder(form.getPriceOrder() == null ? "asc" : form.getPriceOrder());
        return ResponseEntity.ok(productSpecification.filter(form, currentPage-1));
    }

    @ApiOperation(value = "Thêm sản phẩm mới")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Thành công"),
    		@ApiResponse(code = 400, message = "Thất bại")
    })
    @PostMapping
    ResponseEntity<ProductEntity> create(@RequestBody(required = true) ProductForm form) {
        try {
            form.setThumbnailUrl(productService.saveImageFile(form.getFile(), CommonUtils.toSlug(form.getName())));
            return ResponseEntity.ok(productService.save(form));
        } catch (IOException | ImageNotExtensionException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @ApiOperation(value = "Xem chi tiết sản phẩm")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Thành công"),
    		@ApiResponse(code = 400, message = "Thất bại")
    })
    @GetMapping("/detail/{id}")
    ResponseEntity<ProductEntity> detail(@PathVariable long id){
        return ResponseEntity.ok(productService.findById(id).get());
    }

    @ApiOperation(value = "Sửa chi tiết sản phẩm")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Thành công"),
    		@ApiResponse(code = 400, message = "Thất bại")
    })
    @PutMapping("/detail/{id}")
    ResponseEntity detail(@PathVariable long id, @RequestBody(required = true) ProductForm form) throws ImageNotExtensionException, IOException {
        ProductEntity entity = productService.findById(id).get();
        if(null != form.getFile()){
            form.setThumbnailUrl(productService.saveImageFile(form.getFile(), entity.getSlug()));
            entity.setThumbnailUrl(form.getThumbnailUrl());
        }
        entity.setName(form.getName());
        entity.setPrice(form.getPrice());
        entity.setShortDescription(form.getShortDescription());
        entity.setDescription(form.getDescription());
        entity.setCategory(categoryService.findById(form.getCategoryId()).get());
        entity.setBrand(brandService.findById(form.getBrandId()).get());
        entity.setStartDatetime(LocalDateTime.parse(form.getStartDatetime(), DateTimeFormatter.ISO_DATE_TIME));
        if(StringUtils.hasText(form.getEndDatetime())){
            entity.setEndDatetime(LocalDateTime.parse(form.getEndDatetime(), DateTimeFormatter.ISO_DATE_TIME));
        }
        entity.setUpdateDatetime(LocalDateTime.now());
        productService.save(entity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @ApiOperation(value = "Xóa sản phẩm")
    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable long id){
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
