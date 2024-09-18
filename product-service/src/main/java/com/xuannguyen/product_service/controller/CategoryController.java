package com.xuannguyen.product_service.controller;

import com.xuannguyen.product_service.dto.request.CreationCategoryRequset;
import com.xuannguyen.product_service.dto.response.ApiResponse;
import com.xuannguyen.product_service.entity.Category;
import com.xuannguyen.product_service.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "*",maxAge = 3600)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    @Operation(summary="Lấy danh sách danh mục")
    public ApiResponse<List<Category>>getListCategory(){
        List<Category> categories = categoryService.findAll();
        ApiResponse<List<Category>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categories);
        return apiResponse;
    }

    @GetMapping("/enabled")
    @Operation(summary="Lấy ra danh sách danh mục đã kích hoạt")
    public ApiResponse<List<Category>> getListEnabled(){
        List<Category> categories = categoryService.getListEnabled();
        ApiResponse<List<Category>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categories);
        return apiResponse;
    }


    @PostMapping("/create")
    @Operation(summary="Tạo mới danh mục")
    public ApiResponse createCategory(@Valid @RequestBody CreationCategoryRequset request){
        Category category = categoryService.createCategory(request);

        return ApiResponse.builder()
                .message("Thêm danh mục thành công")
                .result(category)
                .build();
    }

    @PutMapping("/update/{id}")
    @Operation(summary="Tìm danh mục bằng id và cập nhật danh mục đó")
    public ApiResponse updateCategory(@PathVariable String id, @Valid @RequestBody CreationCategoryRequset request){

        Category category = categoryService.updateCategory(id, request);
        return ApiResponse.builder()
                .message("Cập nhật thành công")
                .result(category)
                .build();
    }

    @PutMapping("/enable/{id}")
    @Operation(summary="Kích hoạt danh mục bằng id")
    public ApiResponse enabled(@PathVariable String id){
        categoryService.enableCategory(id);
        return ApiResponse.builder()
                .message("cập nhật thành công danh mục")
                .build();

    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary="Xóa danh mục bằng id")
    public ApiResponse delete(@PathVariable String id){
        categoryService.deleteCategory(id);
        return ApiResponse.builder()
                .message("xóa thành công")
                .build();
    }


}

