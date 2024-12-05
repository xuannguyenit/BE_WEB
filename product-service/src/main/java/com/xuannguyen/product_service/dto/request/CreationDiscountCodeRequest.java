package com.xuannguyen.product_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreationDiscountCodeRequest {
    @NonNull
    @Size(min = 1, max = 20)
    private String code;

    private long discountPercentage;
}
