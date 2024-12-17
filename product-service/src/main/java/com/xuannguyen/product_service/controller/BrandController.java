package com.xuannguyen.product_service.controller;

import com.xuannguyen.product_service.dto.request.CreationBrandRequest;

import com.xuannguyen.product_service.dto.response.ApiResponse;
import com.xuannguyen.product_service.dto.response.PageResponse;
import com.xuannguyen.product_service.entity.Brand;

import com.xuannguyen.product_service.entity.Category;
import com.xuannguyen.product_service.service.BrandService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/brands")
//@CrossOrigin(origins = "*",maxAge = 3600)
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("/")
    @Operation(summary="Lấy danh sách danh mục")
    public ApiResponse getAllBrands(){
        List<Brand> brands = brandService.getAllBrands();
        return ApiResponse.builder()
                .result(brands)
                .build();
    }

    @GetMapping("/enabled")
    @Operation(summary="Lấy ra danh sách thương hiệu đã kích hoạt")
    public ApiResponse<List<Brand>> getListEnabled(){
        List<Brand> brands = brandService.getListBrandsEnable();
        ApiResponse<List<Brand>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(brands);
        return apiResponse;
    }
    @GetMapping("/{id}")
    public ApiResponse<Brand> getBrandById(@PathVariable String id){
        Brand brand = brandService.getBrandById(id);
        return ApiResponse.<Brand>builder()
                .code(200)
                .message("success")
                .result(brand)
                .build();
    }


    @PostMapping("/create")
    @Operation(summary="Tạo mới danh mục")
    public ApiResponse creationBrand(@Valid @RequestBody CreationBrandRequest request){
        Brand brand = brandService.saveBrand(request);

        return ApiResponse.builder()
                .message("Thêm thành công thương hiệu")
                .result(brand)
                .build();
    }

    @PutMapping("/update/{id}")
    @Operation(summary="Tìm danh mục bằng id và cập nhật danh mục đó")
    public ApiResponse updateBrand(@PathVariable String id, @Valid @RequestBody CreationBrandRequest request){
        Brand brand = brandService.updateBrand(id, request);

        return ApiResponse.builder()
                .message("Cập nhật thành công")
                .result(brand)
                .build();
    }

    @PutMapping("/enable/{id}")
    @Operation(summary="Kích hoạt danh mục bằng id")
    public ApiResponse enabled(@PathVariable String id){
        brandService.enableBrand(id);
        return ApiResponse.builder()
                .message("Cập nhật thành công")
                .build();
    }

    @DeleteMapping("/delete/{id}")

    public ApiResponse delete(@PathVariable String id){
        brandService.deleteBrand(id);
        return ApiResponse.builder()
                .message("xóa thành công")
                .build();
    }


    @GetMapping ("/listbrand/get")
    public ApiResponse<PageResponse<Brand>> getAllCategory(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "9") int size
    ) {
        if (page < 1 || size < 1) {
            throw new IllegalArgumentException("Page and size must be greater than 0.");
        }
        PageResponse<Brand> pageResponse = brandService.getAllBrandPagination(page - 1, size);

        return ApiResponse.<PageResponse<Brand>>builder()
                .result(pageResponse)
                .build();
    }

    @PatchMapping("/shortdelete/{id}")
    public ApiResponse<Brand> shortDelete(@PathVariable String id){

        return ApiResponse.<Brand>builder()
                .result(brandService.shortDeleteBrand(id))// phương thức xóa mềm
                .message("xóa mềm thành công")
                .build();
    }
//    @GetMapping ("/get/all/brand")
//    PageResponse<Brand> getAllBrand( @RequestParam(value = "page", required = false, defaultValue = "1") int page,
//                                     @RequestParam(value = "size", required = false, defaultValue = "9") int size){
//
//    }



}

