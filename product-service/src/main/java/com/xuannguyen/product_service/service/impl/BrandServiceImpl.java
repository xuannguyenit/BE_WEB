package com.xuannguyen.product_service.service.impl;

import com.xuannguyen.product_service.dto.request.CreationBrandRequest;
import com.xuannguyen.product_service.entity.Brand;
import com.xuannguyen.product_service.exception.AppException;
import com.xuannguyen.product_service.exception.ErrorCode;
import com.xuannguyen.product_service.repository.BranRepository;
import com.xuannguyen.product_service.service.BrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)

public class BrandServiceImpl implements BrandService {
    BranRepository branRepository;
    @Override
    public List<Brand> getAllBrands() {

        List<Brand> brands = branRepository.findAll();
        return brands;
    }

    @Override
    public Brand getBrandById(String id) {
        Brand brand = branRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.BRAND_NOT_EXITS));
        return brand;
    }

    @Override
    public Brand saveBrand(CreationBrandRequest request) {
        if (branRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }
        Brand brand  = new Brand();
        brand.setName(request.getName());
        brand.setEnable(true);
        branRepository.save(brand);
        return brand;
    }

    @Override
    public Brand updateBrand(String id,CreationBrandRequest request) {
        if (!branRepository.existsById(id)){
            throw new AppException(ErrorCode.BRAND_NOT_EXITS);
        }
        Brand brand  = branRepository.findById(id).get();
        brand.setName(request.getName());
        branRepository.save(brand);
        return brand;

    }

    @Override
    public void deleteBrand(String id) {
        if (!branRepository.existsById(id)){
            throw new AppException(ErrorCode.BRAND_NOT_EXITS);
        }
        branRepository.deleteById(id);
    }

    @Override
    public Brand findBrandByName(String brandName) {
        return null;
    }

    @Override
    public void enableBrand(String id) {
        Brand brand = branRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.BRAND_NOT_EXITS));
        if (brand.isEnable()){
            brand.setEnable(false);
        }else {
            brand.setEnable(true);
        }
    }

    @Override
    public List<Brand> getListBrandsEnable() {
        List<Brand> brands = branRepository.findALLByEnabled();
        return brands;
    }
}
