package com.xuannguyen.product_service.service.impl;

import com.xuannguyen.product_service.dto.request.CreationCategoryRequset;
import com.xuannguyen.product_service.entity.Category;
import com.xuannguyen.product_service.exception.AppException;
import com.xuannguyen.product_service.exception.ErrorCode;
import com.xuannguyen.product_service.repository.CategoryRepository;
import com.xuannguyen.product_service.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        List<Category> list = categoryRepository.findAll();
        return list;
    }

    @Override
    public List<Category> getListEnabled() {
        return categoryRepository.findALLByEnabled();
    }

    @Override
    public Category createCategory(CreationCategoryRequset request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXITS);
        }
        Category category = new Category();
        category.setName(request.getName());
        category.setEnable(true);

       return categoryRepository.save(category);

    }

    @Override
    public Category updateCategory(String id, CreationCategoryRequset request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXITS);
        }
        Category category = categoryRepository.findById(id).get();
        category.setName(request.getName());
        return categoryRepository.save(category);

    }

    @Override
    public void enableCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXITS));
        if (category.isEnable())
        {
            category.setEnable(false);
        }else {
            category.setEnable(true);
        }
    }

    @Override
    public void deleteCategory(String id) {
        if (!categoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXITS);
        }
        categoryRepository.deleteById(id);
    }
}
