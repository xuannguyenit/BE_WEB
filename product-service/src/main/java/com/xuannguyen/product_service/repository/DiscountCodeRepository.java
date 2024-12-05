package com.xuannguyen.product_service.repository;

import com.xuannguyen.product_service.entity.DiscountCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface DiscountCodeRepository extends JpaRepository<DiscountCode, String> {
    boolean existsByCode(String code);

}
