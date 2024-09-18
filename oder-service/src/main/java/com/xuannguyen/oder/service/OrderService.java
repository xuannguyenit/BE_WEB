package com.xuannguyen.oder.service;

import com.xuannguyen.oder.dto.request.CreateOrderRequest;
import com.xuannguyen.oder.entity.Order;
import com.xuannguyen.oder.entity.OrderStatus;

import java.util.List;

public interface OrderService {
    Order createOrderFromCart(CreateOrderRequest request);
    void handlePaymentSuccess(String orderId);
    void completeOrder(String orderId);
    Order getOrderById(String orderId);
    List<Order> getAllOrdersByUserId(String userId);
    void cancelOrder(String orderId);
    Order updateOrderStatus(String orderId, OrderStatus newStatus);

}
