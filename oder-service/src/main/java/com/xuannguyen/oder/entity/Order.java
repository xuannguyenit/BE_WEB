package com.xuannguyen.oder.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter @AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_order")
public class Order {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private String id;
    private String userId;
    private String fullName;
    @Column(name = "email")
    private String email;
    @Column (nullable = false)
    private String phone;
    @Column (name = "address",nullable = false)
    private String address;
    private LocalDateTime orderTime;
    private boolean active;
    private String status; // trạng thái đơn hàng
    private long totalPrice;
    private LocalDateTime shippingDate; // ngày nhận hàng
    @Column(name = "transaction_reference", unique = true, nullable = true)
    private String transactionReference; // mã tham chiếu khi giao dịch thành công
    private String shippingMethod;
    private String shippingAddress;
    private String trackingNumber;
    private String paymentMethod; // phương thức thanh toán
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderDetail> orderDetails = new ArrayList<>();

}
