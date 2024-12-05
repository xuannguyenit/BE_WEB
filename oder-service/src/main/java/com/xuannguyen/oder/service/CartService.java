package com.xuannguyen.oder.service;

import com.xuannguyen.oder.entity.CartItem;

import java.util.List;

public interface CartService {
    List<CartItem> getCartItems(String cartId);
    List<CartItem> getCartByUserId(String userId);
}
