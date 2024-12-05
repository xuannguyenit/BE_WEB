package com.xuannguyen.oder.service.impl;

import com.xuannguyen.oder.dto.request.OrderUserRequest;
import com.xuannguyen.oder.dto.request.ProductUpdateQuantityRequest;
import com.xuannguyen.oder.dto.respone.ApiResponse;
import com.xuannguyen.oder.dto.respone.Product;
import com.xuannguyen.oder.dto.respone.RevenueDto;
import com.xuannguyen.oder.entity.Cart;
import com.xuannguyen.oder.entity.Order;
import com.xuannguyen.oder.entity.OrderDetail;
import com.xuannguyen.oder.exception.AppException;
import com.xuannguyen.oder.exception.ErrorCode;
import com.xuannguyen.oder.repository.CartRepository;
import com.xuannguyen.oder.repository.OrderDetailRepository;
import com.xuannguyen.oder.repository.OrderRepository;
import com.xuannguyen.oder.repository.httpclient.ProductClient;
import com.xuannguyen.oder.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdeServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;


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




    // lấy ra danh sách order theo userId
    @Override
    public List<Order> getOrderUserByUserId(String userId) {
        List<Order> list = orderRepository.findByUserId(userId);
        return list;
    }
    // danh sách các đơn hàng của User đã thanh toan thành công (Lịch sử mua bán)
    @Override
    public List<Order> getOrderUserByOrderIdComplete() {
        String userId =SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId==null){
            return null;
        }
        // lấy ra danh sách tất cả các đơn hàng đã thanh toán thành công
        List<Order> listO = orderRepository.getAllByUserIdComplete(userId);
        return listO;
    }
    // thống kê các mặt hàng mà user đã mua
    @Override
    public List<OrderDetail> getProductOfUser() {
        String userId =SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId==null){
            return null;
        }
        // lấy ra danh sách tất cả các đơn hàng đã thanh toán thành công
        List<Order> listO = orderRepository.getAllByUserIdComplete(userId);

        List<OrderDetail> listODetail = new ArrayList<OrderDetail>();
        for (Order order : listO) {
            List<OrderDetail> listItemOrderDetailOfOrder = order.getOrderDetails();
            for (OrderDetail o : listItemOrderDetailOfOrder) {
                OrderDetail orderDetail1 = new OrderDetail();
                orderDetail1.setProductId(o.getProductId());
                orderDetail1.setProductName(o.getProductName());
                orderDetail1.setProductPrice(o.getProductPrice());
                orderDetail1.setProductQuantity(o.getProductQuantity());
                orderDetail1.setTotalPrice(o.getTotalPrice());
                orderDetail1.setProductSalePrice(o.getProductSalePrice());
                listODetail.add(orderDetail1);
            }

        }
        return listODetail;
    }

    @Override
    public List<OrderDetail> getAllOderDetail(String orderId) {
        return List.of();
    }

    @Override
    public Order getOrderById(String id) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.CART_NOT_EXITS));
        return order;
    }
    @Override
    public Order createOrderFromCart(OrderUserRequest orderRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId==null){
            return null;
        }
        // Lấy giỏ hàng của user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXITS));

        if (cart.getCartItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_EMPTY);
        }

        // Tạo đối tượng Order
        Order order = Order.builder()
                .userId(userId)
                .fullName(orderRequest.getFullName())
                .email(orderRequest.getEmail())
                .phone(orderRequest.getPhone())
                .address(orderRequest.getAddress())
                .orderTime(LocalDateTime.now())
                .status("Pending") // Trạng thái mặc định ban đầu
                .totalPrice(cart.getTotalPrice())

                .build();

        // Lưu Order vào DB
        Order savedOrder = orderRepository.save(order); // tạo ra Order trước khi tạo ra các OrderDetail

        // Ánh xạ CartItem sang OrderDetail
        List<OrderDetail> orderDetails = cart.getCartItems().stream().map(cartItem -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(savedOrder);
            orderDetail.setProductId(cartItem.getProductId());
            orderDetail.setProductName(cartItem.getProductName());
            orderDetail.setProductPrice(cartItem.getProductPrice());
            orderDetail.setProductQuantity(cartItem.getProductQuantity());
            orderDetail.setProductSalePrice(cartItem.getDiscountPercentage()); // phần trăm giảm giá
            orderDetail.setTotalPrice(cartItem.getTotalPrice());
            return orderDetail;
        }).toList();
        Cart cartTest = cartRepository.save(cart);
        // Lưu danh sách OrderDetail vào DB
        orderDetailRepository.saveAll(orderDetails);



        return savedOrder;
    }
// update lại trạng thái của Order sau khi thanh toán xong
@Override
public Order updateOrder(String id) {
    // Lấy thông tin đơn hàng
    Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

    if(order.getStatus().equals("Complete")){
        return order;
    }
    // Lấy request hiện tại để truy cập URL
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    // Lấy mã transactionReference (vnp_TxnRef) từ URL
    String transactionReference = request.getParameter("vnp_TxnRef");
    // Lấy danh sách chi tiết đơn hàng
    List<OrderDetail> orderDetails = order.getOrderDetails();

    // Cập nhật trạng thái của từng sản phẩm
    orderDetails.forEach(orderDetail -> {
        try {
            // Tạo request để cập nhật số lượng sản phẩm
            ProductUpdateQuantityRequest productUpdateQuantityRequest = new ProductUpdateQuantityRequest();
            productUpdateQuantityRequest.setProductId(orderDetail.getProductId());
            productUpdateQuantityRequest.setQuantity(orderDetail.getProductQuantity());

            // Gọi API để cập nhật
            ApiResponse<Product> response = productClient.updateProductQuantityAfterOrder(productUpdateQuantityRequest);

            // Kiểm tra kết quả trả về
            if (response == null ) {
                throw new AppException(ErrorCode.PRODUCT_NOT_EXITS
                        );
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXITS);
        }
    });

    // Cập nhật trạng thái đơn hàng
    order.setOrderTime(LocalDateTime.now());
    order.setStatus("Complete");
    order.setTransactionReference(transactionReference);// cần set mã vnp_TxnRef trả về từ vnpay
    order.setOrderTime(LocalDateTime.now());
    Order saveOrder =orderRepository.save(order);
    // Xóa giỏ hàng hoặc cập nhật trạng thái giỏ hàng
    // Lấy giỏ hàng của user
//    String userId = saveOrder.getUserId();
//
//    Cart cart = cartRepository.findByUserId(userId)
//            .orElseGet(() -> {return null;});
//    if (cart!=null){
//    cartRepository.delete(cart);}

    return saveOrder;
}
    // thống kê tất cả các đơn hàng của user
    @Override
    public List<Order> getAllOrderByUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userId==null){
            return null;
        }
        //lấy ra danh sách các đơn hàng của user hiện tại
        List<Order> orders = orderRepository.getAllByUserId(userId);
        return orders;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RevenueDto getRevenueStatistics() {
        List<Order> list = orderRepository.getAllOrderCompleted();
        long revenue = 0;
        for (Order order : list) {
            revenue += order.getTotalPrice();
        }
        return RevenueDto.builder()
                .revenue(revenue)
                .build();
    }

    @Override
    public long getRevenueStatisticsByTime(LocalDate dateStart, LocalDate dateEnd) {
        return 0;
    }
    // tất cả danh sách đơn hàng
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> getAllOrderDesc() {
        List<Order> list = orderRepository.getAllOrderDesc();
        return list;
    }

}
