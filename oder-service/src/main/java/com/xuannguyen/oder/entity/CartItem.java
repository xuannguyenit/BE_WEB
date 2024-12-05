package com.xuannguyen.oder.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false) // ánh xạ với cột `cart_id` trong database
    @JsonBackReference
    private Cart cart;

    private String productId;
    private String productName;
    private long productPrice;
    private int productQuantity;
    private long discountPercentage;
    private long totalPrice;
    private String userId;

}
