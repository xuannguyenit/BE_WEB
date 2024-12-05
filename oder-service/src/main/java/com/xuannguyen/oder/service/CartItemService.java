package com.xuannguyen.oder.service;

import com.xuannguyen.oder.dto.request.UpdateCartItemRequest;
import com.xuannguyen.oder.dto.respone.CartItemResponse;

public interface CartItemService {
    CartItemResponse addToCart(String productId);
    void removeFromCart(String cartId, String productId);
    CartItemResponse updateCart(UpdateCartItemRequest request);
    void deleteCart(String cartId);
}
