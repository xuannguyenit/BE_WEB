package com.xuannguyen.oder.service.impl;

import com.xuannguyen.oder.dto.request.OrderDetailRequest;
import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.dto.respone.DiscountCode;
import com.xuannguyen.oder.dto.respone.Product;
import com.xuannguyen.oder.entity.OrderDetail;
import com.xuannguyen.oder.entity.Order;
import com.xuannguyen.oder.exception.AppException;
import com.xuannguyen.oder.exception.ErrorCode;
import com.xuannguyen.oder.repository.OrderDetailRepository;
import com.xuannguyen.oder.repository.OrderRepository;
import com.xuannguyen.oder.repository.httpclient.ProductClient;
import com.xuannguyen.oder.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductClient productClient;
    // get sản phẩm theo id
    private Product getProductClientResponse(String id) {
        ApiResponse<Product> productResponse = productClient.getProduct(id);
        if (productResponse == null || productResponse.getResult() == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXITS);
        }
        return productResponse.getResult();
    }
    private DiscountCode getDiscountCodeResponse(String id) {
        ApiResponse<DiscountCode> apiResponseDiscountCodeResponse = productClient.getDiscountCode(id);
        if (apiResponseDiscountCodeResponse == null || apiResponseDiscountCodeResponse.getResult() == null) {
            return null;
        }
        return apiResponseDiscountCodeResponse.getResult();
    }
    @Override
    public OrderDetail createOrderDetail(OrderDetailRequest request) {
        // kiểm tra xem orderId có tồn tại hay không
        Order order = orderRepository.findById(request.getOrderid()).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND));
        // kiểm tra product có tồn tại hay không
        Product productClientResponse = getProductClientResponse(request.getProductId());
        if (productClientResponse == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXITS);
        }
        // kiểm tra mã giảm giá có tồn tại hay không
        DiscountCode discountCode = productClientResponse.getDiscountCode();

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProductId(request.getProductId());
        orderDetail.setProductName(productClientResponse.getName());
        orderDetail.setProductPrice(productClientResponse.getPrice());
        long discountPercentage = 0;
        if (discountCode == null) {
            discountPercentage = 0;
        }
        discountPercentage = discountCode.getDiscountPercentage();
        orderDetail.setProductSalePrice(discountPercentage);
        orderDetail.setProductQuantity(request.getProductQuantity());

        // Tính tổng tiền có áp dụng giá khuyến mại
        long productPrice = productClientResponse.getPrice();
        long productQuantity = request.getProductQuantity();
        long totalPrice = productQuantity * (productPrice - (productPrice * discountPercentage / 100));
        orderDetail.setTotalPrice(totalPrice);
        orderDetailRepository.save(orderDetail);



        return orderDetail;
    }

    @Override
    public OrderDetail updateOrderDetail(String id, OrderDetailRequest request) {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND));
        Order order = orderRepository.findById(request.getOrderid()).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND));
        // kiểm tra product có tồn tại hay không
        Product productClientResponse = getProductClientResponse(request.getProductId());
        if (productClientResponse == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXITS);
        }
        // kiểm tra mã giảm giá có tồn tại hay không
        DiscountCode discountCode = productClientResponse.getDiscountCode();

        orderDetail.setOrder(order);
        orderDetail.setProductId(request.getProductId());
        orderDetail.setProductName(productClientResponse.getName());
        orderDetail.setProductPrice(productClientResponse.getPrice());
        long discountPercentage = 0;
        if (discountCode == null) {
            discountPercentage = 0;
        }
        discountPercentage = discountCode.getDiscountPercentage();
        orderDetail.setProductSalePrice(discountPercentage);
        orderDetail.setProductQuantity(request.getProductQuantity());

        // Tính tổng tiền có áp dụng giá khuyến mại
        long productPrice = productClientResponse.getPrice();
        long productQuantity = request.getProductQuantity();
        long totalPrice = productQuantity * (productPrice - (productPrice * discountPercentage / 100));
        orderDetail.setTotalPrice(totalPrice);
        orderDetailRepository.save(orderDetail);

        return null;
    }

    @Override
    public void deleteOrderDetail(String id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> getAllOrderDetail() {
        return orderDetailRepository.findAll();
    }

    @Override
    public List<OrderDetail> getOrderDetailByOrderId(String id) {
        List<OrderDetail> list = orderDetailRepository.findByOrderId(id);
        return list;
    }

    @Override
    public OrderDetail getOrderDetailById(String id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderDetail;
    }
    // addtocart

    @Override
    public OrderDetail addToCart(String userId, OrderDetailRequest request) {
        // Kiểm tra xem giỏ hàng của user đã tồn tại chưa
        Order order = orderRepository.findByUserIdAndStatus(userId, "PENDING")
                .orElseGet(() -> {
                    // Nếu chưa tồn tại, tạo mới giỏ hàng
                    Order newOrder = new Order();
                    newOrder.setUserId(userId);
                    newOrder.setStatus("PENDING");
                    newOrder.setTotalPrice(0L); // Tổng giá ban đầu
                    return orderRepository.save(newOrder);
                });

        // Kiểm tra sản phẩm có tồn tại không
        Product product = getProductClientResponse(request.getProductId());

        // Tìm tất cả sản phẩm trong giỏ hàng theo orderId và productId
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderIdAndProductId(order.getId(), request.getProductId());

        OrderDetail existingOrderDetail = null;
        if (!orderDetails.isEmpty()) {
            existingOrderDetail = orderDetails.get(0); // Lấy bản ghi đầu tiên
        }

        if (existingOrderDetail != null) {
            // Nếu sản phẩm đã có, cập nhật số lượng
            int newQuantity = existingOrderDetail.getProductQuantity() + request.getProductQuantity();
            existingOrderDetail.setProductQuantity(newQuantity);

            // Tính lại tổng giá
            long discountPercentage = product.getDiscountCode() != null
                    ? product.getDiscountCode().getDiscountPercentage()
                    : 0;
            existingOrderDetail.setTotalPrice(
                    calculateTotalPrice(product.getPrice(), newQuantity, discountPercentage)
            );

            orderDetailRepository.save(existingOrderDetail);

            // Cập nhật tổng giá của giỏ hàng
            updateOrderTotalPrice(order);
            return existingOrderDetail;
        }

        // Nếu sản phẩm chưa có, tạo mới OrderDetail
        long discountPercentage = product.getDiscountCode() != null
                ? product.getDiscountCode().getDiscountPercentage()
                : 0;

        OrderDetail newOrderDetail = new OrderDetail();
        newOrderDetail.setOrder(order);
        newOrderDetail.setProductId(request.getProductId());
        newOrderDetail.setProductName(product.getName());
        newOrderDetail.setProductPrice(product.getPrice());
        newOrderDetail.setProductQuantity(request.getProductQuantity());
        newOrderDetail.setProductSalePrice(discountPercentage);
        newOrderDetail.setTotalPrice(
                calculateTotalPrice(product.getPrice(), request.getProductQuantity(), discountPercentage)
        );

        orderDetailRepository.save(newOrderDetail);

        // Cập nhật tổng giá của giỏ hàng
        updateOrderTotalPrice(order);
        return newOrderDetail;
    }


    private void updateOrderTotalPrice(Order order) {
        // Tính tổng giá mới dựa trên tất cả các OrderDetail trong giỏ hàng
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(order.getId());
        long newTotalPrice = orderDetails.stream()
                .mapToLong(OrderDetail::getTotalPrice)
                .sum();
        order.setTotalPrice(newTotalPrice);
        orderRepository.save(order);
    }

    private long calculateTotalPrice(long productPrice, long quantity, long discountPercentage) {
        return quantity * (productPrice - (productPrice * discountPercentage / 100));
    }

}
