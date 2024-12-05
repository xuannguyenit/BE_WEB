package com.xuannguyen.oder.service;

import com.xuannguyen.oder.dto.request.OrderDetailRequest;
import com.xuannguyen.oder.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    // create orderdetail
    OrderDetail createOrderDetail(OrderDetailRequest request);
    // update orderdetail
    OrderDetail updateOrderDetail(String id, OrderDetailRequest request);
    // delete orderdetail
    void deleteOrderDetail(String id);
    // get all
    List<OrderDetail> getAllOrderDetail();
    // get orderdetail by oderId
    List<OrderDetail> getOrderDetailByOrderId(String id);
    OrderDetail getOrderDetailById(String id);
    OrderDetail addToCart(String userId, OrderDetailRequest request);
}
