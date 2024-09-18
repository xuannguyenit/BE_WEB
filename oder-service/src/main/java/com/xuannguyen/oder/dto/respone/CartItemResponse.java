package com.xuannguyen.oder.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder

public class CartItemResponse {

    private String productId;
    private String productName;
    private int quantity;
    private long price;
//    private double coupon;
    private long totalPrice;
}
