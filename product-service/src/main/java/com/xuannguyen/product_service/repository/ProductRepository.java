package com.xuannguyen.product_service.repository;

import com.xuannguyen.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    @Query(value = "Select * from Product order by id desc limit :number",nativeQuery = true)
    List<Product> getListNewest(int number);

    @Query(value = "Select * from Product order by price limit 8 ",nativeQuery = true)
    List<Product> getListByPrice();

    @Query(value ="Select * from Product where category_id = :id order by rand() limit 4",nativeQuery = true)
    List<Product> findRelatedProduct(String id);

    @Query(value ="Select * from Product where category_id = :id",nativeQuery = true)
    List<Product> getListProductByCategory(String id);

    @Query(value = "Select * from Product where category_id = :id and price between :min and :max",nativeQuery = true)
    List<Product> getListProductByPriceRange(String id,int min,int max);

    @Query(value= "Select p from Product p where p.name like %:keyword% ")
    List<Product> searchProduct(String keyword);
    @Query(value = "Select * from Product where name= :name", nativeQuery = true )
    Product getProductByName(String name);
    boolean existsByName(String name);
    // phân trang cho tất cả sản phẩm
    Page<Product> findAll(Pageable pageable);
}
