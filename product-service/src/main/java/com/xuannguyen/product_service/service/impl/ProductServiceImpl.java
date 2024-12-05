package com.xuannguyen.product_service.service.impl;

import com.xuannguyen.product_service.dto.request.CreationProductRequest;
import com.xuannguyen.product_service.dto.request.ProductQuantityRequest;
import com.xuannguyen.product_service.dto.response.PageResponse;

import com.xuannguyen.product_service.entity.*;
import com.xuannguyen.product_service.exception.AppException;
import com.xuannguyen.product_service.exception.ErrorCode;
import com.xuannguyen.product_service.mapper.ProductMapper;
import com.xuannguyen.product_service.repository.*;

import com.xuannguyen.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private BranRepository  branRepository;
    @Autowired
    private DiscountCodeRepository discountCodeRepository;

    private ProductMapper productMapper;

    private String formatPrice(long price) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return currencyFormat.format(price);
    }

    @Override
    public List<Product> getList() {

        return productRepository.findAll(Sort.by("createDate").descending());
    }

    @Override
    public Product getProductById(String id) {

        Product product= productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXITS));

        return product;
    }


    @Override
    public Product createProduct(CreationProductRequest request) {

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setShortDescription(request.getShortDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_EXITS));
        product.setCategory(category);
        Brand brand = branRepository.findById(request.getBrandId()).orElseThrow(()-> new AppException(ErrorCode.BRAND_NOT_EXITS));
        product.setBrand(brand);

        Set<Image> images = new HashSet<>();
        for(String imageId: request.getImageIds()){
            Image image = imageRepository.findById(imageId).orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_EXITS));
            images.add(image);
        }
        product.setImages(images);

        if (request.getDiscountCodeId() == null) {
            product.setDiscountCode(null);
        }else {
            DiscountCode discountCode = discountCodeRepository.findById(request.getDiscountCodeId()).orElseThrow(()->new AppException(ErrorCode.INVALID_DISCOUNTCODE));
            product.setDiscountCode(discountCode);
        }
        productRepository.save(product);
        return product;
    }

    @Override
    public Product updateProduct(String id, CreationProductRequest request) {

        Product product= productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXITS));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_EXITS));
        product.setCategory(category);
        Brand brand = branRepository.findById(request.getBrandId()).orElseThrow(()-> new AppException(ErrorCode.BRAND_NOT_EXITS));
        product.setBrand(brand);

        Set<Image> images = new HashSet<>();
        for(String imageId: request.getImageIds()){
            Image image = imageRepository.findById(imageId).orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_EXITS));
            images.add(image);
        }
        product.setImages(images);

        if (request.getDiscountCodeId() == null) {
            product.setDiscountCode(null);
        }else {
            DiscountCode discountCode = discountCodeRepository.findById(request.getDiscountCodeId()).orElseThrow(()->new AppException(ErrorCode.INVALID_DISCOUNTCODE));
            product.setDiscountCode(discountCode);
        }
        productRepository.save(product);
        return product;
    }


    @Override
    public void deleteProduct(String id) {

        Product product= productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXITS));
        product.getImages().remove(this);
        productRepository.delete(product);
    }
    // phân trang tất cho tất cả sản phẩm trong hệ thống
    @Override
    public PageResponse<Product> getAllProductPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        var pageResponse = productRepository.findAll(pageable);

        return PageResponse.<Product>builder()
                .currentPage(page)
                .pageSize(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .data(pageResponse.getContent())
                .build();
    }
    // phân trang cho tất cả các sản phẩm dc giảm giá
    @Override
    public PageResponse<Product> getAllProductSale (int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        var pageResponse = productRepository.findDiscountedProducts(pageable);
        return PageResponse.<Product>builder()
                .currentPage(page)
                .pageSize(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .data(pageResponse.getContent())
                .build();
    }

    @Override
    public Product updateProductAfterOrder(ProductQuantityRequest request) {
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXITS));
        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);
        return product;
    }

    @Override
    public Product updateProductAfterImport(ProductQuantityRequest request) {
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXITS));
        product.setQuantity(product.getQuantity() + request.getQuantity());
        productRepository.save(product);
        return product;
    }

    @Override
    public List<Product> getProductByCategory(String categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products;
    }

    // phân trang cho sản phẩm có mã giảm giá theo id mã giảm giá
    @Override
    public PageResponse<Product> getDiscountedProductsPagination(String discountCodeId,int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<Product> pageResponse = productRepository.findDiscountedProducts(discountCodeId,pageable);

        return PageResponse.<Product>builder()
                .currentPage(page)
                .pageSize(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .data(pageResponse.getContent())
                .build();
    }


    @Override
    public PageResponse<Product> getProductsByCategoryPagination(String categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<Product> pageResponse = productRepository.findByCategoryId(categoryId, pageable);

        return PageResponse.<Product>builder()
                .currentPage(page)
                .pageSize(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .data(pageResponse.getContent())
                .build();
    }


    // update lại số lượng sản phẩm trong db khi người dùng mua hàng
    @Override
    public Product updateProductQuantity(String id, Integer quantity) {
        Product product= productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXITS));
        product.setQuantity(quantity);
        return product;
    }

    @Override
    public Product addDiscountForProduct(String id, String discountId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXITS));
        DiscountCode discountCode = discountCodeRepository.findById(discountId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DISCOUNTCODE_NOTEXIT));

        // Lấy các mã giảm giá hiện có
        product.setDiscountCode(discountCode);

        return productRepository.save(product);
    }



    @Override
    public List<Product> getListNewst(int number) {

        List<Product> list = productRepository.getListNewest(number);
        return list;
    }

    @Override
    public List<Product> getListByPrice() {

        return productRepository.getListByPrice();
    }
    @Override
    public List<Product> findRelatedProduct(String id){
        List<Product> list = productRepository.findRelatedProduct(id);
        return list;
    }

    @Override
    public List<Product> getListProductByCategory(String id){
        List<Product> list =productRepository.getListProductByCategory(id);
        return list;
    }

    @Override
    public List<Product> getListByPriceRange(String id,int min, int max){
        List<Product> list =productRepository.getListProductByPriceRange(id, min, max);
        return list;
    }

    @Override
    public List<Product> searchProduct(String keyword) {

        List<Product> list = productRepository.searchProduct(keyword);
        return list;
    }

}
