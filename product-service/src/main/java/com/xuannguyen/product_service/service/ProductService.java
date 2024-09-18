package com.xuannguyen.product_service.service;

import com.xuannguyen.product_service.dto.request.CreationProductRequest;
import com.xuannguyen.product_service.dto.response.PageResponse;
import com.xuannguyen.product_service.dto.response.ProductResponse;
import com.xuannguyen.product_service.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getList();

    List<Product> getListNewst(int number);

    List<Product> getListByPrice();

    List<Product> findRelatedProduct(String id);

    List<Product> getListProductByCategory(String id);

    List<Product> getListByPriceRange(String id,int min, int max);

    List<Product> searchProduct(String keyword);

    Product getProductById(String id);

    Product createProduct(CreationProductRequest request);

    Product updateProduct(String id, CreationProductRequest request);

    void deleteProduct(String id);

    // phân trang
    PageResponse<Product> getAllProductPagination(int page, int size);
    // update lại số lượng cho sản phẩm
    Product updateProductQuantity(String id, Integer quantity);

}
