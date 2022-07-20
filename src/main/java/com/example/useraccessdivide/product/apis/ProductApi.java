package com.example.useraccessdivide.product.apis;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.useraccessdivide.common.Pagingation;
import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.product.dtos.ProductDto;
import com.example.useraccessdivide.product.entities.Product;
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
@RequestMapping("/api/v1/product")
@Api(value = "Product api", tags = { "api sản phẩm" })
public class ProductApi {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductSpecification productSpecification;

	@ApiOperation(value = "Xem hình ảnh sản phẩm")
	@GetMapping(path = "/image/{fileName}", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE })
	ResponseEntity<byte[]> viewImage(@PathVariable String fileName) throws IOException {
		return ResponseEntity.ok(productService.getByteImage(fileName));
	}

	@ApiOperation(value = "Xem và tìm kiếm sản phẩm")
	@GetMapping
	ResponseEntity<Pagingation<Product>> productView(ProductSearchForm form,
			@RequestParam(name = "page", defaultValue = "1") int currentPage) {
		form.setPriceOrder(form.getPriceOrder() == null ? "asc" : form.getPriceOrder());
		return ResponseEntity.ok(productSpecification.filter(form, currentPage - 1));
	}

	@ApiOperation(value = "Thêm sản phẩm mới")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@PostMapping
	ResponseEntity<Product> create(@Valid ProductForm form) throws MyException {
		DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		try {
			Product entity = new Product();
			entity.setName(form.getName());
			entity.setSlug(CommonUtils.toSlug(entity.getName()));
			entity.setPrice(Integer.parseInt(form.getPrice()));
			entity.setThumbnailUrl(productService.saveImageFile(form.getFile(), CommonUtils.toSlug(form.getName())));
			entity.setShortDescription(form.getShortDescription());
			entity.setDescription(form.getDescription());
			entity.setCategoryId(Long.parseLong(form.getCategoryId()));	
			entity.setBrandId(Long.parseLong(form.getBrandId()));
			entity.setStartDatetime(LocalDateTime.parse(form.getStartDatetime(), dtf));
			if (StringUtils.hasText(form.getEndDatetime())) {
				entity.setEndDatetime(LocalDateTime.parse(form.getEndDatetime(), dtf));
			}
			entity.setCreateDatetime(LocalDateTime.now());

			return ResponseEntity.ok(productService.save(entity));
		} catch (IOException e) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0010", "MSG_W0009", "lưu hình ảnh");
		} catch (ImageNotExtensionException e) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0009", "MSG_W0008");
		}
	}

	@ApiOperation(value = "Xem chi tiết sản phẩm")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@GetMapping("/detail/{id}")
	ResponseEntity<ProductDto> detail(@PathVariable long id) throws MyException {
		Product entityResult = productService.findById(id);
		ProductDto dto = new ProductDto();
		BeanUtils.copyProperties(entityResult, dto);
		return ResponseEntity.ok(dto);
	}

	@ApiOperation(value = "Sửa chi tiết sản phẩm")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Thành công"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@PutMapping("/detail/{id}")
	ResponseEntity<?> detail(@PathVariable long id, ProductForm form)
			throws ImageNotExtensionException, IOException, MyException {
		Product entity = productService.findById(id);
		if (null != form.getFile()) {
			entity.setThumbnailUrl(productService.saveImageFile(form.getFile(), entity.getSlug()));
		}
		entity.setName(form.getName());
		entity.setPrice(Integer.parseInt(form.getPrice()));
		entity.setShortDescription(form.getShortDescription());
		entity.setDescription(form.getDescription());
		entity.setCategoryId(Long.parseLong(form.getCategoryId()));	
		entity.setBrandId(Long.parseLong(form.getBrandId()));
		entity.setStartDatetime(LocalDateTime.parse(form.getStartDatetime(), DateTimeFormatter.ISO_DATE_TIME));
		if (StringUtils.hasText(form.getEndDatetime())) {
			entity.setEndDatetime(LocalDateTime.parse(form.getEndDatetime(), DateTimeFormatter.ISO_DATE_TIME));
		}
		entity.setUpdateDatetime(LocalDateTime.now());
		productService.save(entity);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@ApiOperation(value = "Xóa sản phẩm")
	@DeleteMapping("/{id}")
	ResponseEntity<?> delete(@PathVariable long id) {
		productService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
