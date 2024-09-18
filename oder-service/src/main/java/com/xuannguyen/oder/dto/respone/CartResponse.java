package com.xuannguyen.oder.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class CartResponse {
    private String id;
    private String userId;
    private HashMap<String,CartItemResponse> items;
    private double total;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder

    public static class ProductClientRespone {
        private String name;
        private String description;
        private long price;
        private int quantity;
        private String categoryId;
        private String brandId;
        private Set<String> imageIds;
        private LocalDate createDate;
    }
}
