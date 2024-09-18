package com.xuannguyen.oder.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "tbl_oder")
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    private String firstname; // bắt buộc

    private String lastname;

    private String country; // bắt buộc

    private String address;// địa chỉ người nhận bắt buộc

    private String town;

    private String state;// tên khu vực

    private long postCode;// mã bưu chính

    private String email; // bắt buộc

    private String phone; // băt buộc

    private String note;// chú ý

    private long totalPrice;

    private String paymentmethod; // phương thức thanh toán
    private OrderStatus status;

    @OneToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;

}
