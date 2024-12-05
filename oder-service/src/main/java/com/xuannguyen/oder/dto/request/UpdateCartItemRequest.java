package com.xuannguyen.oder.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCartItemRequest {
    private String cartId;
    private int quantity;
}
