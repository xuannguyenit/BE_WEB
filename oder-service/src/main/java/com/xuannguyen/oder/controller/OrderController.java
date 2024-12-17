package com.xuannguyen.oder.controller;

import com.xuannguyen.oder.dto.request.OrderUserRequest;
import com.xuannguyen.oder.dto.request.OrderUpdateRequest;
import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.dto.respone.DailyRevenueResponse;
import com.xuannguyen.oder.dto.respone.RevenueDto;
import com.xuannguyen.oder.entity.Order;
import com.xuannguyen.oder.entity.OrderDetail;
import com.xuannguyen.oder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping ("/create")
    public ApiResponse<Order> createOrder(@RequestBody OrderUserRequest request){
        return ApiResponse.<Order>builder()
                .message("create order success")
                .result(orderService.createOrderFromCart(request))
                .build();
    }
    // update lại trạng thái của order sau khi thanh toán hoàn thành
    @PutMapping("/update/status/{id}")
    public ApiResponse<Order> updateOrderStatus(@PathVariable String id){
        return ApiResponse.<Order>builder()
                .message("update order status success")
                .result(orderService.updateOrder(id))
                .build();
    }
    @PutMapping("/update/{id}")
    public ApiResponse<Order> updateOrder(@PathVariable String id, @RequestBody OrderUpdateRequest request){
        return ApiResponse.<Order>builder()
                .message("update order success")
                .result(null)
                .build();
    }
    @GetMapping("/get/{userid}")
    public ApiResponse<List<Order>> getOrderByUser(@PathVariable String userid){
        return ApiResponse.<List<Order>>builder()
                .message("update order success")
                .result(orderService.getOrderUserByUserId(userid))
                .build();
    }
    @GetMapping ("/{id}") // get ra order theo id
    public ApiResponse<Order> getOrderById(@PathVariable String id){
        return ApiResponse.<Order>builder()
                .message("select order by id success")
                .result(orderService.getOrderById(id))
                .build();
    }
    // thống kê danh sách các mặt hàng mà user đã thực hiện mua(dành cho trang client)
    @GetMapping ("/listorderdetail/user/get")
    public ApiResponse<List<OrderDetail>> getOrderDetailByUser(){
        return ApiResponse.<List<OrderDetail>>builder()
                .message("select order detail by id success")
                .result(orderService.getProductOfUser())
                .build();
    }
    // thống kê các đơn hàng của user hiện tại đăng nhập gồm cả các đơn hàng đã thanh toán và chưa thanh toán
    @GetMapping ("/allorder/user/get")
    public ApiResponse<List<Order>> getAllOrderByUser(){
        return ApiResponse.<List<Order>>builder()
                .message("select order by id success")
                .result(orderService.getAllOrderByUserId())
                .build();
    }
    // thống kê doanh thu
    @GetMapping ("/get/revenue")
    public ApiResponse<RevenueDto> getRevenue(){
        return ApiResponse.<RevenueDto>builder()
                .message("get revenue success")
                .result(orderService.getRevenueStatistics())
                .build();
    }
    // thống kế tất cả các đơn hàng
    @GetMapping("/all/order/get")
    public ApiResponse<List<Order>> getAllOrder(){
        return ApiResponse.<List<Order>>builder()
                .result(orderService.getAllOrderDesc())
                .build();
    }
    //Thống doanh thu từng ngày trong tháng
    @GetMapping("/get/revenue/day")
    public ApiResponse<List<DailyRevenueResponse>> getRevenueByDay(
            @RequestParam int month,
            @RequestParam int year
    ){
        return ApiResponse.<List<DailyRevenueResponse>>builder()
                .message("get revenueByDay success")
                .result(orderService.getDailyRevenueForMonth(month,year))
                .build();
    }

    // Thống kê tổng đơn hàng
    @GetMapping ("/get/count/order")
    public ApiResponse<Long> getCountOrder(){
        return ApiResponse.<Long>builder()
                .message("get count order success")
                .result(orderService.getCountOrder())
                .build();
    }
}
