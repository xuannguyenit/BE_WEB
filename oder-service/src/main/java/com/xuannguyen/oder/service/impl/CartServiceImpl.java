package com.xuannguyen.oder.service.impl;

import com.xuannguyen.oder.entity.Cart;
import com.xuannguyen.oder.entity.CartItem;
import com.xuannguyen.oder.repository.CartItemRepository;
import com.xuannguyen.oder.repository.CartRepository;
import com.xuannguyen.oder.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    /**
     * Lấy danh sách CartItem trong giỏ hàng
     */
    @Override
    public List<CartItem> getCartItems(String cartId) {
        return cartItemRepository.findAllByCartId(cartId);
    }

    @Override
    public List<CartItem> getCartByUserId(String userId) {
        return cartItemRepository.findAllByUserId(userId);
    }

    /**
     * Xóa giỏ hàng và toàn bộ CartItem liên quan
     */
    public void clearCart(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartRepository.delete(cart);
    }
}
