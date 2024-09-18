package com.xuannguyen.oder.repository;

import com.xuannguyen.oder.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUserIdAndStatus(String userId, String status);
    List<Cart> findAllByUserId(String userId);
}
