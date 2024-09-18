package com.xuannguyen.oder.service.impl;

import com.xuannguyen.oder.dto.request.CreateOrderRequest;
import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.dto.respone.ProductClientResponse;
import com.xuannguyen.oder.entity.Cart;
import com.xuannguyen.oder.entity.CartItem;
import com.xuannguyen.oder.entity.Order;
import com.xuannguyen.oder.entity.OrderStatus;
import com.xuannguyen.oder.exception.AppException;
import com.xuannguyen.oder.exception.ErrorCode;
import com.xuannguyen.oder.repository.CartRepository;
import com.xuannguyen.oder.repository.OrderRepository;
import com.xuannguyen.oder.repository.httpclient.ProductClient;
import com.xuannguyen.oder.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderServiceImpl implements OrderService {

    CartRepository cartRepository;
    OrderRepository orderRepository;
    ProductClient productClient;

    // Lấy thông tin sản phẩm từ ProductClient
    private ProductClientResponse getProductClientResponse(String id) {
        ApiResponse<ProductClientResponse> apiResponseProductResponse = productClient.getProductById(id);
        if (apiResponseProductResponse == null || apiResponseProductResponse.getResult() == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXITS);
        }
        return apiResponseProductResponse.getResult();
    }

    // Tạo đơn hàng mới từ giỏ hàng
    public Order createOrderFromCart(CreateOrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // Lấy userId từ SecurityContextHolder

        // Tìm giỏ hàng đang hoạt động của người dùng
        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE")
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setUserId(userId);
        order.setFirstname(request.getFirstname());
        order.setLastname(request.getLastname());
        order.setCountry(request.getCountry());
        order.setAddress(request.getAddress());
        order.setTown(request.getTown());
        order.setState(request.getState());
        order.setPostCode(request.getPostCode());
        order.setEmail(request.getEmail());
        order.setPhone(request.getPhone());
        order.setNote(request.getNote());
        order.setPaymentmethod(request.getPaymentmethod());
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);  // Đặt trạng thái đơn hàng là PENDING
        order.setCart(cart);  // Liên kết đơn hàng với giỏ hàng

        // Lưu đơn hàng
        Order savedOrder = orderRepository.save(order);

        // Xử lý các sản phẩm trong giỏ hàng
        for (CartItem item : cart.getCartItems()) {
            // Giảm số lượng sản phẩm
            ProductClientResponse product = getProductClientResponse(item.getProductId());
            int newQuantity = product.getQuantity() - item.getQuantity();
            if (newQuantity < 0) {
                throw new AppException(ErrorCode.INSUFFICIENT_QUANTITY);//Insufficient quantity
            }
            productClient.updateProductAfterOrderSuccess(item.getProductId(), newQuantity); // gọi đến productclient để cập nhật số lượng của sản phẩm
        }

        // Đánh dấu giỏ hàng là đã checkout
        cart.setStatus("CHECKED_OUT");  // Cập nhật trạng thái giỏ hàng thành CHECKED_OUT
        cartRepository.save(cart);

        return savedOrder;
    }
    // Xử lý thanh toán thành công
    public void handlePaymentSuccess(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.PROCESSING);  // Chuyển trạng thái đơn hàng sang PROCESSING
            orderRepository.save(order);
        }
    }

    // Cập nhật trạng thái khi thanh toán hoàn tất
    public void completeOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() == OrderStatus.PROCESSING) {
            order.setStatus(OrderStatus.COMPLETED);  // Chuyển trạng thái đơn hàng sang COMPLETED
            orderRepository.save(order);
        }
    }


    // Lấy đơn hàng theo ID
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    // Lấy tất cả đơn hàng của người dùng
    public List<Order> getAllOrdersByUserId(String userId) {
        return orderRepository.findAllByUserId(userId);
    }


    // Hủy đơn hàng
    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_CANCELLED);
        }

        // Khôi phục số lượng sản phẩm
        for (CartItem item : order.getCart().getCartItems()) {
            ProductClientResponse product = getProductClientResponse(item.getProductId());
            int originalQuantity = product.getQuantity() + item.getQuantity();
            productClient.updateProductAfterOrderSuccess(item.getProductId(), originalQuantity);
        }

        // Cập nhật trạng thái đơn hàng thành CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        // Cập nhật trạng thái giỏ hàng (nếu cần)
        Cart cart = order.getCart();
        cart.setStatus("ACTIVE");
        cartRepository.save(cart);
    }


    // Cập nhật trạng thái của đơn hàng
    public Order updateOrderStatus(String orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
