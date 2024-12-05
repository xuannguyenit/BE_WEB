package com.xuannguyen.oder.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
public class OrderDetailRequest {
    private String orderid;
    private String productId;

    private int productQuantity;

}
