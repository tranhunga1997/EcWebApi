package com.example.useraccessdivide.product.services;

import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.product.entities.Category;
import com.example.useraccessdivide.product.entities.Product;
import com.example.useraccessdivide.product.entities.meta.ProductSearch_;
import com.example.useraccessdivide.product.exceptions.ImageNotExtensionException;
import com.example.useraccessdivide.product.forms.ProductForm;
import com.example.useraccessdivide.product.repositories.ProductRepository;
import com.example.useraccessdivide.product.specifications.ProductSpecification;
import net.bytebuddy.TypeCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * Xem tất cả thông tin sản phẩm
     * @param pageable
     * @return thông tin sản phẩm
     */
    private Page<Product> findAll(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    /**
     * Xem thông tin sản phẩm còn hạn bán
     * @return thông tin sản phẩm
     */
    public List<Product> findProductSellExpiryDate(Pageable pageable){
         List<Product> entities = findAll(pageable).stream().filter(e ->
         {
             if(e.getEndDatetime() == null){
                 return true;
             }
             return e.getEndDatetime().isAfter(LocalDateTime.now());
         }).collect(Collectors.toList());
         return entities;
    }

    /**
     * xem thông tin sản phẩm hết hạn bán
     * @return thông tin sản phẩm
     */
    public List<Product> findProductSellExpiredDate(Pageable pageable){
        List<Product> entities = findAll(pageable).stream().filter(e -> {
            if(e.getEndDatetime() == null){
                return false;
            }
            return e.getEndDatetime().isBefore(LocalDateTime.now());
        }).collect(Collectors.toList());
        return entities;
    }

    /**
     * Tìm kiếm thông tin sản phẩm theo Id
     * @param id mã sản phẩm
     * @return thông tin sản phẩm
     * @throws MyException
     */
    public Product findById(long id) throws MyException{
    	Optional<Product> productOptional = productRepository.findById(id);
    	if(productOptional.isEmpty()) {
    		throw new MyException(HttpStatus.BAD_REQUEST, "0008", "MSG_W0003", "thông tin sản phẩm");
    	}
        return productOptional.get();
    }

    /**
     * Tìm kiếm thông tin sản phẩm theo slug
     * @param slug slug sản phẩm
     * @return thông tin sản phẩm
     * @throws MyException
     */
    public Product findBySlug(String slug) throws MyException{
    	Optional<Product> productOptional = productRepository.findBySlug(slug);
    	if(productOptional.isEmpty()) {
    		throw new MyException(HttpStatus.BAD_REQUEST, "0008", "MSG_W0003", "thông tin sản phẩm");
    	}
        return productOptional.get();
    }

    /**
     * Tìm kiếm thông tin sản phẩm theo category slug
     * @param categorySlug
     * @return thông tin sản phẩm
     */
    public List<Product> findByCategorySlug(String categorySlug){
        Category entity = categoryService.findBySlug(categorySlug).get();
        return productRepository.findByCategory(entity);
    }

    /**
     * Thêm thông tin sản phẩm mới
     * @param product
     * @return thông tin sản phẩm mới
     */
    public Product save(Product product){
        return productRepository.save(product);
    }

    @Deprecated
    public Product save(ProductForm form) {
        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
        Product entity = new Product();
        entity.setName(form.getName());
        entity.setSlug(CommonUtils.toSlug(entity.getName()));
		entity.setPrice(Integer.parseInt(form.getPrice()));
//        entity.setThumbnailUrl(form.getThumbnailUrl());
        entity.setShortDescription(form.getShortDescription());
        entity.setDescription(form.getDescription());
		entity.setCategoryId(Long.parseLong(form.getCategoryId()));	
		entity.setBrandId(Long.parseLong(form.getBrandId()));
        entity.setStartDatetime(LocalDateTime.parse(form.getStartDatetime(), dtf));
        if(StringUtils.hasText(form.getEndDatetime())){
            entity.setEndDatetime(LocalDateTime.parse(form.getEndDatetime(), dtf));
        }
        entity.setCreateDatetime(LocalDateTime.now());
        return productRepository.save(entity);
    }

    /**
     * Xóa thông tin sản phẩm theo id
     * @param id
     */
    public void delete(long id){
        productRepository.deleteById(id);
    }

    /**
     * Kiểm tra đuôi định dạng hình ảnh
     * @param fileName
     * @return {@code true} là định dạng hình ảnh, {@code false} không phải định dạng hình ảnh
     */
    public boolean isImage(String fileName){
        if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".svg")){
            return true;
        }
        return false;
    }

    /**
     * tạo đường dẫn hình ảnh từ tên ảnh
     * @param fileName
     * @return đường dẫn hình ảnh
     */
    private String getPathImage(String fileName){
        File file = new File(rootPath, fileName);
        return file.getAbsolutePath();
    }

    /**
     * đọc byte hình ảnh
     * @param path
     * @return byte hình ảnh
     * @throws IOException
     */
    public byte[] getByteImage(String fileName) throws IOException {
    	String path = getPathImage(fileName);
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);
        fis.close();
        return bytes;
    }

    /**
     * lấy đuôi định dạng hình ảnh
     * @param fileName
     * @return định dạng hình ảnh (.jpg, .jpeg,...) 
     * @throws ImageNotExtensionException
     */
    private String getImageExtension(String fileName) throws ImageNotExtensionException {
        if(!isImage(fileName)){
            throw new ImageNotExtensionException();
        }
        String[] splits = fileName.split("\\.");
        return splits[splits.length-1];
    }

    /**
     * Lưu hình ảnh 
     * <br/>tên hình ảnh đặt theo product slug
     * @param multipartFile
     * @param productSlug
     * @return tên hình ảnh + định dạng
     * @throws IOException
     * @throws ImageNotExtensionException
     */
    public String saveImageFile(MultipartFile multipartFile, String productSlug) throws IOException, ImageNotExtensionException {
        String extension = getImageExtension(multipartFile.getOriginalFilename());
        String fileName = productSlug+"."+extension;
        File file = new File(getPathImage(fileName));
        FileCopyUtils.copy(multipartFile.getBytes(),file);
        return fileName;
    }
}
