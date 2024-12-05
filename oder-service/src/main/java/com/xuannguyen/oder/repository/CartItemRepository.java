package com.xuannguyen.oder.repository;

import com.xuannguyen.oder.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
    // Lấy danh sách CartItem theo cartId
    List<CartItem> findAllByCartId(String cartId);
    //lấy danh sách sản phẩm mua theo userId
    List<CartItem> findAllByUserId(String userId);

    // Tìm CartItem theo cartId và productId
    Optional<CartItem> findByCartIdAndProductId(String cartId, String productId);
}
