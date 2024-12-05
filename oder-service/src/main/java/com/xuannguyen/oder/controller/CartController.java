package com.xuannguyen.oder.controller;

import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.entity.CartItem;
import com.xuannguyen.oder.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {
    // xem chi tiết cart (xem  danh sách các item trong cart)
    @Autowired
    private CartService cartService;
    @GetMapping("/get/{userId}")
    public ApiResponse<List<CartItem>> getCartById (@PathVariable String userId) {
        return ApiResponse.<List<CartItem>>builder()
                .message("success")
                .result(cartService.getCartByUserId(userId))
                .build();
    }
}
