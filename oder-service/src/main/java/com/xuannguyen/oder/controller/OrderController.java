package com.xuannguyen.oder.controller;

import com.xuannguyen.oder.dto.request.CreateOrderRequest;
import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.entity.Order;
import com.xuannguyen.oder.entity.OrderStatus;
import com.xuannguyen.oder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Tạo đơn hàng mới từ giỏ hàng
    @PostMapping("")
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrderFromCart(request);
        return ResponseEntity.ok(ApiResponse.<Order>builder()
                .result(order)
                .build());
    }

    // Xử lý thanh toán thành công
    @PostMapping("/{orderId}/payment-success")
    public ResponseEntity<ApiResponse<Void>> handlePaymentSuccess(@PathVariable String orderId) {
        orderService.handlePaymentSuccess(orderId);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    // Cập nhật trạng thái khi thanh toán hoàn tất
    @PostMapping("/{orderId}/complete")
    public ResponseEntity<ApiResponse<Void>> completeOrder(@PathVariable String orderId) {
        orderService.completeOrder(orderId);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    // Lấy đơn hàng theo ID
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable String orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponse.<Order>builder()
                .result(order)
                .build());
    }

    // Lấy tất cả đơn hàng của người dùng
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrdersByUserId(@PathVariable String userId) {
        List<Order> orders = orderService.getAllOrdersByUserId(userId);
        return ResponseEntity.ok(ApiResponse.<List<Order>>builder()
                .result(orders)
                .build());
    }

    // Hủy đơn hàng
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable String orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    // Cập nhật trạng thái của đơn hàng
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable String orderId,
            @RequestParam OrderStatus newStatus) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok(ApiResponse.<Order>builder()
                .result(updatedOrder)
                .build());
    }
}
