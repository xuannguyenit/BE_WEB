package com.xuannguyen.product_service.service.impl;

import com.xuannguyen.product_service.dto.request.CreationProductRequest;
import com.xuannguyen.product_service.dto.response.PageResponse;

import com.xuannguyen.product_service.entity.Brand;
import com.xuannguyen.product_service.entity.Category;
import com.xuannguyen.product_service.entity.Image;
import com.xuannguyen.product_service.entity.Product;
import com.xuannguyen.product_service.exception.AppException;
import com.xuannguyen.product_service.exception.ErrorCode;
import com.xuannguyen.product_service.mapper.ProductMapper;
import com.xuannguyen.product_service.repository.BranRepository;
import com.xuannguyen.product_service.repository.CategoryRepository;
import com.xuannguyen.product_service.repository.ImageRepository;
import com.xuannguyen.product_service.repository.ProductRepository;

import com.xuannguyen.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        if (productRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.NAME_EXITSTED);
        }

        Product product = new Product();
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
    // update lại số lượng sản phẩm trong db khi người dùng mua hàng
    @Override
    public Product updateProductQuantity(String id, Integer quantity) {
        Product product= productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXITS));
        product.setQuantity(quantity);
        return product;
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
