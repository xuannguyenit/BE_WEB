package com.xuannguyen.product_service.controller;

import com.xuannguyen.product_service.dto.request.CreationDiscountCodeRequest;
import com.xuannguyen.product_service.dto.response.ApiResponse;
import com.xuannguyen.product_service.entity.DiscountCode;
import com.xuannguyen.product_service.service.DiscountCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discount")
public class DiscountCodeController {
    @Autowired
    private DiscountCodeService discountCodeService;
    // thêm mã giảm giá
    @PostMapping ("/post")
    public ApiResponse<DiscountCode> createDiscountCode (@RequestBody CreationDiscountCodeRequest request) {
        return ApiResponse.<DiscountCode>builder()
                .result(discountCodeService.createdDiscountCode(request))
                .build();
    }
    // update mã giảm giá
    @PutMapping ("/put/{id}")
    public ApiResponse<DiscountCode> updateDiscountCode (@PathVariable("id") String id, @RequestBody CreationDiscountCodeRequest request) {
        discountCodeService.updateDiscountCode(id, request);
        return ApiResponse.<DiscountCode>builder()
                .result(discountCodeService.updateDiscountCode(id, request))
                .build();
    }
    // xóa mã giảm giá
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Void> deleteDiscountCode (@PathVariable("id") String id) {
        discountCodeService.deleteDiscountCode(id);
        return ApiResponse.<Void>builder()
                .build();
    }
    // get list mã giảm giá
    @GetMapping("/get")
    public  ApiResponse<List<DiscountCode>> getDiscountCode (){
     List<DiscountCode>  list = discountCodeService.findAllDiscountCode();
     return ApiResponse.<List<DiscountCode>>builder()
             .result(list)
             .build();
    }
    @GetMapping("/get/{id}")
    public  ApiResponse<DiscountCode> getDiscountCode (@PathVariable("id") String id) {
        DiscountCode discountCode = discountCodeService.findDiscountCode(id);
        return ApiResponse.<DiscountCode>builder().result(discountCode).build();
    }
}
