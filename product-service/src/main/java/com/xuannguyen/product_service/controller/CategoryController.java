package com.xuannguyen.product_service.controller;

import com.xuannguyen.product_service.dto.request.CreationCategoryRequset;
import com.xuannguyen.product_service.dto.response.ApiResponse;
import com.xuannguyen.product_service.dto.response.PageResponse;
import com.xuannguyen.product_service.entity.Category;
import com.xuannguyen.product_service.entity.Product;
import com.xuannguyen.product_service.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
//@CrossOrigin(origins = "*",maxAge = 3600)
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
    @GetMapping ("/{id}")
    public ApiResponse<Category> getCategoryById (@PathVariable String id){
        Category category = categoryService.getCategoryById(id);
        return ApiResponse.<Category>builder()
                .code(200)
                .message("success")
                .result(category)
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
    @GetMapping ("/listcate/get")
    public ApiResponse<PageResponse<Category>> getAllCategory(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "9") int size
    ) {
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("Page and size must be greater than 0.");
        }
        PageResponse<Category> pageResponse = categoryService.getAllCategoryPagination(page - 1, size);

        return ApiResponse.<PageResponse<Category>>builder()
                .result(pageResponse)
                .build();
    }

    @PatchMapping ("/shortdelete/{id}")
    public ApiResponse<Category> shortDelete(@PathVariable String id){
        return ApiResponse.<Category>builder()
                .result(categoryService.shortDeleteCategory(id))
                .message("xóa thành công (xóa mềm)")
                .build();
    }



}

