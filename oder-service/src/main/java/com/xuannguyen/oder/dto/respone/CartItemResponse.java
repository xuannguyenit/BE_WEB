package com.xuannguyen.oder.dto.respone;

import com.xuannguyen.oder.entity.Cart;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    private String id;
    private String cartId;
    private String productId;
    private String productName;
    private long productPrice;
    private int productQuantity;
    private long discountPercentage;
    private long totalPrice;
    private String userId;
}
