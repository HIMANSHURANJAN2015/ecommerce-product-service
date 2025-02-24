package com.scaler.ecommerceproductservice.services;

import com.scaler.ecommerceproductservice.models.Product;
import java.util.List;

public interface ProductService {
    /*
        Since we need to have 2 implemenations, 1 with getting model info from repo classes
        and one by getting model info from FskeStoreAPI, so we are creating interface.
    */

    Product getProductById(long id);
    List<Product> getAllProducts();
    Product createProduct(String name, String desc, double price, String imageURL, String category);
}
