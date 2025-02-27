package com.scaler.ecommerceproductservice.services;

import com.scaler.ecommerceproductservice.models.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productDBService")
public class ProductDBService implements ProductService {

    @Override
    public Product getProductById(long id){
        return null;
    }

    @Override
    public List<Product> getAllProducts(){
        return List.of();
    }

    @Override
    public Product createProduct(String name, String desc, double price, String imageURL, String category){
        return null;
    }

    @Override
    public Product updateProduct(long id, String name, String desc, double price, String imageURL, String category){
        return null;
    }

    @Override
    public void deleteProduct(long id){

    }
}
