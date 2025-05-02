package com.scaler.ecommerceproductservice.services;

import com.scaler.ecommerceproductservice.models.Product;

public interface ProductAIService {
    Product createProductWithAIDescription(String name, double price, String imageUrl, String category);
}