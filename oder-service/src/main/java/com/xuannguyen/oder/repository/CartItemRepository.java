package com.xuannguyen.oder.repository;

import com.xuannguyen.oder.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    boolean existsByProductId(String productId);
}
