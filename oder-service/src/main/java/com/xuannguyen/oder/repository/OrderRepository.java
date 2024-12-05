package com.xuannguyen.oder.repository;

import com.xuannguyen.oder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUserId(String userid);
    Optional<Order> findByUserIdAndStatus(String userId, String status);
    @Query(value = "SELECT o FROM Order o WHERE o.userId = :userId AND o.status = 'complete'")
    List<Order> getAllByUserIdComplete(@Param("userId") String userId);
    @Query (value = "select  o from Order  o where  o.userId= :userId ORDER BY o.orderTime DESC")// thống kê tất cả các đơn hàng của user
    List<Order> getAllByUserId(@Param("userId") String userId);

    // lấy ra tất cả các đơn hàng đã thanh toán
    @Query(value = "SELECT o FROM Order o WHERE o.status = 'Complete'")
    List<Order> getAllOrderCompleted();
    // danh sách tất cả đơn hàng
    @Query (value = "select  o from Order  o  ORDER BY o.orderTime DESC")
    List<Order> getAllOrderDesc();


}
