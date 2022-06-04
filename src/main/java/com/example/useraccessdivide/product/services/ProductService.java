package com.example.useraccessdivide.product.services;

import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.product.entities.CategoryEntity;
import com.example.useraccessdivide.product.entities.ProductEntity;
import com.example.useraccessdivide.product.entities.meta.ProductSearch_;
import com.example.useraccessdivide.product.exceptions.ImageNotExtensionException;
import com.example.useraccessdivide.product.forms.ProductForm;
import com.example.useraccessdivide.product.repositories.ProductRepository;
import com.example.useraccessdivide.product.specifications.ProductSpecification;
import net.bytebuddy.TypeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductService {
    @Value("${file.upload.image.path}")
    private String rootPath;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductSpecification productSpecification;

    private List<ProductEntity> findAll(){
        return productRepository.findAll(Sort.by("id"));
    }

    /**
     * not use
     * @param priceOrder
     * @return
     */
    private List<ProductEntity> findAll(String priceOrder) {
        if(priceOrder.equalsIgnoreCase("asc")) {
            return productRepository.findAll(Sort.by(Sort.Order.asc(ProductSearch_.PRICE)));
        } else {
            return productRepository.findAll(Sort.by(Sort.Order.desc(ProductSearch_.PRICE)));
        }
    }

    /**
     * not use
     * @return
     */
    public List<ProductEntity> findProductSellExpiryDate(){
         List<ProductEntity> entities = findAll().stream().filter(e ->
         {
             if(e.getEndDatetime() == null){
                 return true;
             }
             return e.getEndDatetime().isAfter(LocalDateTime.now());
         }).collect(Collectors.toList());
         return entities;
    }

    /**
     * not use
     * @param priceOrder
     * @return
     */
    public List<ProductEntity> findProductSellExpiryDate(String priceOrder){
        List<ProductEntity> entities = findAll(priceOrder).stream().filter(e ->
        {
            if(e.getEndDatetime() == null){
                return true;
            }
            return e.getEndDatetime().isAfter(LocalDateTime.now());
        }).collect(Collectors.toList());
        return entities;
    }

    /**
     * not use
     * @return
     */
    public List<ProductEntity> findProductSellExpiredDate(){
        Stream<ProductEntity> entityStream = findAll().stream().filter(e -> {
            if(e.getEndDatetime() == null){
                return false;
            }
            return e.getEndDatetime().isBefore(LocalDateTime.now());
        });
        return entityStream.collect(Collectors.toList());
    }

    /**
     * not use
     * @param priceOrder
     * @return
     */
    public List<ProductEntity> findProductSellExpiredDate(String priceOrder){
        Stream<ProductEntity> entityStream = findAll(priceOrder).stream().filter(e -> {
            if(e.getEndDatetime() == null){
                return false;
            }
            return e.getEndDatetime().isBefore(LocalDateTime.now());
        });
        return entityStream.collect(Collectors.toList());
    }

    public Optional<ProductEntity> findById(long id){
        return productRepository.findById(id);
    }

    public Optional<ProductEntity> findBySlug(String slug){
        return productRepository.findBySlug(slug);
    }

    public List<ProductEntity> findByCategorySlug(String categorySlug){
        CategoryEntity entity = categoryService.findBySlug(categorySlug).get();
        return productRepository.findByCategory(entity);
    }

    public ProductEntity save(ProductEntity product){
        if(product.getSlug() == null || StringUtils.hasText(product.getSlug())){
            product.setSlug(product.getName());
        }
        return productRepository.save(product);
    }

    public ProductEntity save(ProductForm form) {
        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
        ProductEntity entity = new ProductEntity();
        entity.setName(form.getName());
        entity.setSlug(CommonUtils.toSlug(entity.getName()));
        entity.setPrice(form.getPrice());
        entity.setThumbnailUrl(form.getThumbnailUrl());
        entity.setShortDescription(form.getShortDescription());
        entity.setDescription(form.getDescription());
        entity.setCategory(categoryService.findById(form.getCategoryId()).get());
        entity.setBrand(brandService.findById(form.getBrandId()).get());
        entity.setStartDatetime(LocalDateTime.parse(form.getStartDatetime(), dtf));
        if(StringUtils.hasText(form.getEndDatetime())){
            entity.setEndDatetime(LocalDateTime.parse(form.getEndDatetime(), dtf));
        }
        entity.setUpdateDatetime(LocalDateTime.now());
        entity.setCreateDatetime(LocalDateTime.now());
        return productRepository.save(entity);
    }

    public void delete(long id){
        productRepository.deleteById(id);
    }

    public boolean isImage(String fileName){
        if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".svg")){
            return true;
        }
        return false;
    }

    public String getPathImage(String fileName){
        File file = new File(rootPath, fileName);
        return file.getAbsolutePath();
    }

    public byte[] getByteImage(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);
        fis.close();
        return bytes;
    }

    private String getImageExtension(String fileName) throws ImageNotExtensionException {
        if(!isImage(fileName)){
            throw new ImageNotExtensionException();
        }
        String[] splits = fileName.split("\\.");
        return splits[splits.length-1];
    }

    public String saveImageFile(MultipartFile multipartFile, String productSlug) throws IOException, ImageNotExtensionException {
        String extension = getImageExtension(multipartFile.getOriginalFilename());
        String fileName = productSlug+"."+extension;
        File file = new File(getPathImage(fileName));
        FileCopyUtils.copy(multipartFile.getBytes(),file);
        return fileName;
    }
}
