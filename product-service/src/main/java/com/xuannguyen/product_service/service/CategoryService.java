package com.xuannguyen.product_service.service;

import com.xuannguyen.product_service.dto.request.CreationCategoryRequset;
import com.xuannguyen.product_service.dto.response.PageResponse;
import com.xuannguyen.product_service.entity.Category;
import com.xuannguyen.product_service.entity.Product;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();

    List<Category> getListEnabled();

    Category createCategory(CreationCategoryRequset request);

    Category updateCategory(String id,CreationCategoryRequset request);

    void enableCategory(String id);

    void deleteCategory(String id);
    List<Category> getTopCategory();
    PageResponse<Category> getAllCategoryPagination(int page, int size);
    Category getCategoryById(String id);
    Category shortDeleteCategory(String id);
}
