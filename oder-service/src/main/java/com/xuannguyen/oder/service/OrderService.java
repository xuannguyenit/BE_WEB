package com.xuannguyen.oder.service;

import com.xuannguyen.oder.dto.request.OrderUserRequest;
import com.xuannguyen.oder.dto.respone.DailyRevenueResponse;
import com.xuannguyen.oder.dto.respone.RevenueDto;
import com.xuannguyen.oder.entity.Order;
import com.xuannguyen.oder.entity.OrderDetail;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    //get all order by user id (thông tin đặt hàng của 1 user) có thể dùng cho trang admin
    List<Order> getOrderUserByUserId(String userId);
    // danh sách các đơn hàng đã mua của user ( đã hoàn thành thanh toán)
    List<Order> getOrderUserByOrderIdComplete();
    // danh sách các mat hàng đã mua của user
    List<OrderDetail> getProductOfUser();
    // thống kê các sản phẩm đã bán
    List<OrderDetail> getAllOderDetail(String orderId);
    // get theo orderId
    Order getOrderById(String id);

    Order createOrderFromCart( OrderUserRequest orderRequest);
    Order updateOrder (String id); // cập nhật lại trạng thái đơn hàng sau khi thanh toán thành công

    // thống kê danh sách các đơn hàng của user (Tất cả đơn hàng kể cả chưa thanh toán và thanh toán)
    List<Order> getAllOrderByUserId(); // note dùng cho trang client (userId có thể get từ token)
    // Thống kê doanh thu từ đầu
    RevenueDto getRevenueStatistics ();
    // thống kê doanh thu theo
    long getRevenueStatisticsByTime(LocalDate dateStart, LocalDate dateEnd);
    List<Order> getAllOrderDesc ();
    List<DailyRevenueResponse> getDailyRevenueForMonth (int month , int year);
    Long getCountOrder();

}
