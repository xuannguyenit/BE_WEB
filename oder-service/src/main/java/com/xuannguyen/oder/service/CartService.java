package com.xuannguyen.oder.service;

import com.xuannguyen.oder.dto.respone.ProductClientResponse;
import com.xuannguyen.oder.entity.Cart;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CartService {
    ProductClientResponse getProductClientResponse(String id);
    Cart addItemToCart(String productId);
    Cart updateCartItemQuantity(String productId, int newQuantity);
    Cart removeItemFromCart(String productId);
    List<Cart> getAllCartsByUserId(String userId);
}
