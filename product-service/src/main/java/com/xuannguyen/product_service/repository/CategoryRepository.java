package com.xuannguyen.product_service.repository;

import com.xuannguyen.product_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query("Select c from Category c where c.enable = true")
    List<Category> findALLByEnabled();
    @Query(value = "select * from category where name = name = :name", nativeQuery = true)
    Category findByName(@Param("name") String name);
    boolean existsByName(String name);
}
