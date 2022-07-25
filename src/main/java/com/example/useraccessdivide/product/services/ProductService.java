package com.example.useraccessdivide.product.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.common.utils.FileUtil;
import com.example.useraccessdivide.product.entities.Category;
import com.example.useraccessdivide.product.entities.Product;
import com.example.useraccessdivide.product.exceptions.ImageNotExtensionException;
import com.example.useraccessdivide.product.repositories.ProductRepository;

@Service
public class ProductService {
	@Value("${file.upload.image.path}")
	private String rootPath;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryService categoryService;

	/**
	 * Tìm tất cả thông tin sản phẩm
	 * 
	 * @param pageable
	 * @return thông tin sản phẩm
	 */
	private Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}
	
	/**
	 * Tìm thông tin sản phẩm
	 * @param ids danh sách id
	 * @return danh sách sản phẩm
	 */
	public List<Product> findAllById(List<Long> ids) {
		return productRepository.findAllById(ids);
	}

	/**
	 * Xem thông tin sản phẩm còn hạn bán
	 * 
	 * @return thông tin sản phẩm
	 */
	public List<Product> findProductSellExpiryDate(Pageable pageable) {
		List<Product> entities = findAll(pageable).stream()
				.filter(e -> e.getEndDatetime() == null || e.getEndDatetime().isAfter(LocalDateTime.now()))
				.collect(Collectors.toList());
		return entities;
	}

	/**
	 * xem thông tin sản phẩm hết hạn bán
	 * 
	 * @return thông tin sản phẩm
	 */
	public List<Product> findProductSellExpiredDate(Pageable pageable) {
		List<Product> entities = findAll(pageable).stream()
				.filter(e -> e.getEndDatetime() == null || e.getEndDatetime().isBefore(LocalDateTime.now()))
				.collect(Collectors.toList());
		return entities;
	}

	/**
	 * Tìm kiếm thông tin sản phẩm theo Id
	 * 
	 * @param id mã sản phẩm
	 * @return thông tin sản phẩm
	 * @throws MyException
	 */
	public Product findById(long id) throws MyException {
		Optional<Product> productOptional = productRepository.findById(id);
		return productOptional
				.orElseThrow(() -> new MyException(HttpStatus.BAD_REQUEST, "0008", "MSG_W0003", "thông tin sản phẩm"));
	}

	/**
	 * Tìm kiếm thông tin sản phẩm theo slug
	 * 
	 * @param slug slug sản phẩm
	 * @return thông tin sản phẩm
	 * @throws MyException
	 */
	public Product findBySlug(String slug) throws MyException {
		Optional<Product> productOptional = productRepository.findBySlug(slug);
		return productOptional
				.orElseThrow(() -> new MyException(HttpStatus.BAD_REQUEST, "0008", "MSG_W0003", "thông tin sản phẩm"));
	}

	/**
	 * Tìm kiếm thông tin sản phẩm theo category slug
	 * 
	 * @param categorySlug
	 * @return thông tin sản phẩm
	 */
	public List<Product> findByCategorySlug(String categorySlug) {
		Category entity = categoryService.findBySlug(categorySlug).get();
		return productRepository.findByCategory(entity);
	}

	/**
	 * Thêm thông tin sản phẩm mới
	 * 
	 * @param product
	 * @return thông tin sản phẩm mới
	 */
	public Product save(Product product) {
		return productRepository.save(product);
	}

	/**
	 * Thêm thông tin sản phẩm sử dụng csv
	 * @param file file csv
	 * @throws MyException
	 */
	public void saveProductWithCsv(MultipartFile file) throws MyException {
		// kiểm tra định dang csv
		if (!file.getContentType().startsWith("text/csv")) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0009", "MSG_W0010");
		}

		// đọc nội dung file
		List<String> rawContents = new ArrayList<String>();
		try {
			BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
			StringBuffer content = new StringBuffer();
			int c;
			while ((c = bis.read()) != -1) {
				content.append((char) c);
			}
			rawContents = Arrays.asList(content.toString().split("\\r\\n"));
			
			// xử lý nội dung
			List<Product> products = rawContents.stream().map(str -> {
				String[] strs = str.split("[,]");
				Product product = new Product();
				product.setName(strs[0]);
				product.setSlug(CommonUtils.toSlug(product.getName()));
				product.setPrice(Integer.parseInt(strs[1]));
				product.setShortDescription(strs[2]);
				product.setDescription(strs[3]);
				product.setStartDatetime(LocalDateTime.parse(strs[4], DateTimeFormatter.ofPattern("yyyy/M/dd H:mm")));
				product.setCategoryId(Long.parseLong(strs[5]));
				product.setBrandId(Long.parseLong(strs[6]));
				product.setCreateDatetime(LocalDateTime.now());
				return product;
			}).collect(Collectors.toList());
			productRepository.saveAll(products);
		} catch (IOException | RuntimeException e) {
			e.printStackTrace();
			throw new MyException(HttpStatus.BAD_REQUEST, "0011", "MSG_W0011");
		}

	}

	/**
	 * Lưu hình ảnh <br/>
	 * tên hình ảnh đặt theo product slug
	 * 
	 * @param multipartFile
	 * @param productSlug
	 * @return tên hình ảnh + định dạng
	 * @throws IOException
	 * @throws ImageNotExtensionException
	 */
	public String saveImageFile(MultipartFile multipartFile, String productSlug)
			throws IOException, ImageNotExtensionException {
		String extension = getImageExtension(multipartFile);
		String fileName = productSlug + "." + extension;
		File file = new File(getPathImage(fileName));
		FileCopyUtils.copy(multipartFile.getBytes(), file);
		return fileName;
	}

	/**
	 * Xóa thông tin sản phẩm theo id
	 * 
	 * @param id
	 */
	public void delete(long id) {
		productRepository.deleteById(id);
	}

	/**
	 * đọc byte hình ảnh
	 * 
	 * @param path
	 * @return byte hình ảnh
	 * @throws IOException
	 */
	public byte[] getByteImage(String fileName) throws IOException {
		String path = getPathImage(fileName);
		File file = new File(path);
		InputStream inp = new BufferedInputStream(new FileInputStream(file));
		byte[] bytes = new byte[inp.available()];
		inp.read(bytes);
		inp.close();
		return bytes;
	}

	/**
	 * tạo đường dẫn hình ảnh từ tên ảnh
	 * 
	 * @param fileName
	 * @return đường dẫn hình ảnh
	 */
	private String getPathImage(String fileName) {
		File file = new File(rootPath, fileName);
		return file.getAbsolutePath();
	}

	/**
	 * lấy đuôi định dạng hình ảnh
	 * 
	 * @param fileName
	 * @return định dạng hình ảnh (.jpg, .jpeg,...)
	 * @throws ImageNotExtensionException
	 */
	private String getImageExtension(MultipartFile file) throws ImageNotExtensionException {
		if (!FileUtil.checkImage(file.getContentType())) {
			throw new ImageNotExtensionException();
		}
		String[] splits = file.getOriginalFilename().split("\\.");
		return splits[splits.length - 1];
	}
}
