package com.xuannguyen.oder.dto.request;

import com.xuannguyen.oder.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayResponse {
    private String orderId;
    private long totalPrice;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private List<OrderDetail> orderDetails; // Chi tiết đơn hàng, nếu cần
}
