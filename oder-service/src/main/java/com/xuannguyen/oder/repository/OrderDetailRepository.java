package com.xuannguyen.oder.repository;

import com.xuannguyen.oder.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    List<OrderDetail> findAllByOrderIdAndProductId(String orderId, String productId);
    List<OrderDetail> findByOrderId(String orderId);
}
