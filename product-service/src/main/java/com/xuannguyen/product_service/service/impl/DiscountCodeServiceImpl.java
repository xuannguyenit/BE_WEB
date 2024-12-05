package com.xuannguyen.product_service.service.impl;

import com.xuannguyen.product_service.dto.request.CreationDiscountCodeRequest;
import com.xuannguyen.product_service.entity.DiscountCode;
import com.xuannguyen.product_service.exception.AppException;
import com.xuannguyen.product_service.exception.ErrorCode;
import com.xuannguyen.product_service.repository.DiscountCodeRepository;
import com.xuannguyen.product_service.service.DiscountCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountCodeServiceImpl implements DiscountCodeService {
    @Autowired
    DiscountCodeRepository discountCodeRepository;
    @Override
    public DiscountCode createdDiscountCode(CreationDiscountCodeRequest request) {

        if(discountCodeRepository.existsByCode(request.getCode())){
            throw new AppException(ErrorCode.DISCOUNTCODE_EXITTED);
        }
        DiscountCode discountCode = new DiscountCode();
        discountCode.setCode(request.getCode());
        discountCode.setDiscountPercentage(request.getDiscountPercentage());
        return discountCodeRepository.save(discountCode);
    }

    @Override
    public DiscountCode updateDiscountCode(String id, CreationDiscountCodeRequest request) {
        DiscountCode discountCode = discountCodeRepository.findById(id).orElseThrow(() ->new AppException(ErrorCode.INVALID_DISCOUNTCODE));
        discountCode.setDiscountPercentage(request.getDiscountPercentage());
        discountCode.setCode(request.getCode());
        discountCodeRepository.save(discountCode);
        return discountCode;
    }

    @Override
    public void deleteDiscountCode(String id) {
        DiscountCode discountCode = discountCodeRepository.findById(id).orElseThrow(() ->new AppException(ErrorCode.INVALID_DISCOUNTCODE));
        discountCodeRepository.delete(discountCode);
    }

    @Override
    public List<DiscountCode> findAllDiscountCode() {
        List<DiscountCode> discountCodeList = discountCodeRepository.findAll();
        return discountCodeList;
    }

    @Override
    public DiscountCode shortDeleteDiscountCode(String id) {
        return null;
    }

    @Override
    public DiscountCode findDiscountCode(String id) {
        DiscountCode discountCode = discountCodeRepository.findById(id).orElseThrow(() ->new AppException(ErrorCode.INVALID_DISCOUNTCODE_NOTEXIT));
        return discountCode;
    }
}
