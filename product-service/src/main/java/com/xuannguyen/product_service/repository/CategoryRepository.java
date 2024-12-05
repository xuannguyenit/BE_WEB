package com.xuannguyen.product_service.repository;

import com.xuannguyen.product_service.entity.Category;
import com.xuannguyen.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query("Select c from Category c where c.enable = true")
    List<Category> findALLByEnabled();
    @Query(value = "select * from category where name = :name", nativeQuery = true)
    Category findByName(@Param("name") String name);
    boolean existsByName(String name);
    @Query("SELECT c FROM Category c WHERE c.enable = true")
    List<Category> findTop10CategoriesByEnabled(Pageable pageable);
    Page<Category> findAll(Pageable pageable);
}
