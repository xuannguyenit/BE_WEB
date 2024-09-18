package com.xuannguyen.oder.controller;

import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.entity.Cart;
import com.xuannguyen.oder.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/items")
    public ResponseEntity<ApiResponse<Cart>> addItemToCart(@RequestParam String productId) {
        Cart updatedCart = cartService.addItemToCart(productId);
        return ResponseEntity.ok(ApiResponse.<Cart>builder()
                .result(updatedCart)
                .build());
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PutMapping("/items")
    public ResponseEntity<ApiResponse<Cart>> updateCartItemQuantity(
            @RequestParam String productId,
            @RequestParam int newQuantity) {
        Cart updatedCart = cartService.updateCartItemQuantity(productId, newQuantity);
        return ResponseEntity.ok(ApiResponse.<Cart>builder()
                .result(updatedCart)
                .build());
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/items")
    public ResponseEntity<ApiResponse<Cart>> removeItemFromCart(@RequestParam String productId) {
        Cart updatedCart = cartService.removeItemFromCart(productId);
        return ResponseEntity.ok(ApiResponse.<Cart>builder()
                .result(updatedCart)
                .build());
    }

    // Lấy tất cả giỏ hàng của người dùng (bao gồm cả các giỏ hàng đã checkout)
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Cart>>> getAllCartsByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // Lấy userId từ SecurityContextHolder

        List<Cart> carts = cartService.getAllCartsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.<List<Cart>>builder()
                .result(carts)
                .build());
    }
}
