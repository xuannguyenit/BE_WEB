package com.xuannguyen.oder.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductClientResponse {
    private String name;
    private String description;
    private long price;
    private int quantity;
    private String categoryId;
    private String brandId;
    private Set<String> imageIds;
    private LocalDate createDate;
}
