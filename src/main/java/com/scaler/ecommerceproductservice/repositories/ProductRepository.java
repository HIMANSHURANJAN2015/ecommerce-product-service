package com.scaler.ecommerceproductservice.repositories;


import com.scaler.ecommerceproductservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  ProductRepository extends JpaRepository<Product, Long> {

    Product save(Product product);

}
