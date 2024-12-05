package com.xuannguyen.product_service.service;

import com.xuannguyen.product_service.dto.request.CreationDiscountCodeRequest;
import com.xuannguyen.product_service.entity.DiscountCode;

import java.util.List;

public interface DiscountCodeService {
    // thêm mã giảm giá
    DiscountCode createdDiscountCode(CreationDiscountCodeRequest request);
    // update mã giảm giá
    DiscountCode updateDiscountCode(String id, CreationDiscountCodeRequest request);
    //xóa mã giảm gia
    void deleteDiscountCode(String id);
    // lấy ra tất cả mã giảm giá
    List<DiscountCode> findAllDiscountCode();
    // xóa mềm
    DiscountCode shortDeleteDiscountCode(String id);
    // get discount code theo id
    DiscountCode findDiscountCode(String id);
}
