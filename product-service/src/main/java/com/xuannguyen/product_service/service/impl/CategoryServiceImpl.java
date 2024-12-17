package com.xuannguyen.product_service.service.impl;

import com.xuannguyen.product_service.dto.request.CreationCategoryRequset;
import com.xuannguyen.product_service.dto.response.PageResponse;
import com.xuannguyen.product_service.entity.Category;
import com.xuannguyen.product_service.entity.Image;
import com.xuannguyen.product_service.entity.Product;
import com.xuannguyen.product_service.exception.AppException;
import com.xuannguyen.product_service.exception.ErrorCode;
import com.xuannguyen.product_service.repository.CategoryRepository;
import com.xuannguyen.product_service.repository.ImageRepository;
import com.xuannguyen.product_service.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    ImageRepository imageRepository;

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

        Category category = new Category();
        category.setName(request.getName());

        category.setEnable(true);

        Set<Image> images = new HashSet<>();
        for(String imageId: request.getImageIds()){
            Image image = imageRepository.findById(imageId).orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_EXITS));
            images.add(image);
        }
        category.setImages(images);

       return categoryRepository.save(category);

    }

    @Override
    public Category updateCategory(String id, CreationCategoryRequset request) {

        Category category = categoryRepository.findById(id).get();
        category.setName(request.getName());

        Set<Image> images = new HashSet<>();
        for(String imageId: request.getImageIds()){
            Image image = imageRepository.findById(imageId).orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_EXITS));
            images.add(image);
        }
        category.setImages(images);
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

    @Override
    public List<Category> getTopCategory() {
        return categoryRepository.findTop10CategoriesByEnabled(PageRequest.of(0, 10));
    }
    @Override
    public PageResponse<Category> getAllCategoryPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        var pageResponse = categoryRepository.findAll(pageable);

        return PageResponse.<Category>builder()
                .currentPage(page)
                .pageSize(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .data(pageResponse.getContent())
                .build();
    }

    @Override
    public Category getCategoryById(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXITS));
        return category;
    }

    @Override
    public Category shortDeleteCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.CATEGORY_NOT_EXITS));
        if (category.isDelete()==true){
            return category;
        }
        category.setDelete(true);
        return categoryRepository.save(category);
    }
}
