package com.xuannguyen.product_service.controller;

import com.xuannguyen.product_service.dto.request.CreationBrandRequest;

import com.xuannguyen.product_service.dto.response.ApiResponse;
import com.xuannguyen.product_service.entity.Brand;

import com.xuannguyen.product_service.service.BrandService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/brands")
@CrossOrigin(origins = "*",maxAge = 3600)
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
    @Operation(summary="Xóa danh mục bằng id")
    public ApiResponse delete(@PathVariable String id){
        brandService.deleteBrand(id);
        return ApiResponse.builder()
                .message("xóa thành công")
                .build();
    }


}

