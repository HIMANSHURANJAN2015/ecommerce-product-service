package com.scaler.ecommerceproductservice.repositories;


import com.scaler.ecommerceproductservice.models.Category;
import com.scaler.ecommerceproductservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface  ProductRepository extends JpaRepository<Product, Long> {

    Product save(Product product);
    List<Product> findAll();
    Optional<Product> findById(long id);

    //M-1 Finding products by category name using Derievd query
    List<Product> findByCategory_Name(String categoryName);
    //M-2 Finding products by category name using HQL query
    @Query("Select p from Product p where p.category.name = :catName")
    List<Product> findProductsByCategoryName(@Param("catName") String catName);
    //M-3 Finding products by category name using Native query
    @NativeQuery("Select * from product where category_id in (select id from category where name=:catName)")
    List<Product> findProductsByCategoryNameNative(@Param("catName") String catName);

    void delete(Product product);

}
