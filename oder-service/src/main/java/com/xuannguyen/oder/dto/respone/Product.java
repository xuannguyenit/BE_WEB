package com.xuannguyen.oder.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Product {
    private String id;
    private String name;
    private String description;
    private long price;
    private DiscountCode discountCode;
    private int quantity;
    private Category category;
    private Brand brand;
    private Set<String> imageIds;
    private LocalDate createDate;
}
