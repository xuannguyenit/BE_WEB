package com.xuannguyen.product_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class DiscountCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NonNull
    private String code;
    private long discountPercentage;
    private LocalDate createDate = LocalDate.now();
}
