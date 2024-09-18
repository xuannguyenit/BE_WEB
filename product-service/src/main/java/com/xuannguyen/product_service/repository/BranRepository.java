package com.xuannguyen.product_service.repository;

import com.xuannguyen.product_service.entity.Brand;
import com.xuannguyen.product_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranRepository extends JpaRepository<Brand , String> {
    @Query (value = "Select * from tbl_brand where name = :name",nativeQuery = true)
    Brand findByName (String name);
    @Query(value = "Select c from Brand c where c.enable = true")
    List<Brand> findALLByEnabled();
    boolean existsByName(String name);
}
