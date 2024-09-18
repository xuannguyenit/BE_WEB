package com.xuannguyen.product_service.service;

import com.xuannguyen.product_service.dto.request.CreationBrandRequest;
import com.xuannguyen.product_service.entity.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> getAllBrands();
    Brand getBrandById(String id);
    Brand saveBrand(CreationBrandRequest request);
    Brand updateBrand(String id,CreationBrandRequest request);
    void deleteBrand(String id);
    Brand findBrandByName(String brandName);
    void enableBrand (String id);
    List<Brand> getListBrandsEnable();
}
