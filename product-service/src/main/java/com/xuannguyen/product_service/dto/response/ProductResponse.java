package com.xuannguyen.product_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductResponse {
    private String name;
    private String description;
    private BigDecimal price;
    private int quantity;
    private String categoryId;
    private String brandId;
    private Set<String> imageIds;
    private LocalDate createDate;

}
