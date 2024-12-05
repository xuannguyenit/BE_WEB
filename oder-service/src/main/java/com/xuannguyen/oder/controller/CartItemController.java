package com.xuannguyen.oder.controller;

import com.xuannguyen.oder.dto.request.UpdateCartItemRequest;
import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.dto.respone.CartItemResponse;
import com.xuannguyen.oder.entity.CartItem;
import com.xuannguyen.oder.service.CartItemService;
import com.xuannguyen.oder.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartitem")
public class CartItemController {
    @Autowired
    CartItemService cartItemService;
    @Autowired
    CartService cartService;
    // thêm sản phẩm vào giỏ hàng addtocart
    @PostMapping("/addtocart")
    public ApiResponse<CartItemResponse> addToCart (@RequestParam String productId) {
     return ApiResponse.<CartItemResponse>builder()
             .message("thêm thành công sản phẩm vào giỏ hàng")
             .result(cartItemService.addToCart(productId))

             .build();
    }
    @PutMapping ("/update")
    public ApiResponse<CartItemResponse> updateCart (@RequestBody UpdateCartItemRequest request) {
        return ApiResponse.<CartItemResponse>builder()
                .message("Cập nhật thành công")
                .result(cartItemService.updateCart(request))
                .build();
    }
    // xóa CartItem
    @DeleteMapping ("/{id}")
    public ApiResponse deleteCart (@PathVariable String id) {
        cartItemService.deleteCart(id);
        return ApiResponse.builder()
                .message("Xóa thành công ")
                .build();
    }
}
