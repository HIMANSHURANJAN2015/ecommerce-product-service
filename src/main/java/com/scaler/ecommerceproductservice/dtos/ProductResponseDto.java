package com.scaler.ecommerceproductservice.dtos;

import com.scaler.ecommerceproductservice.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    //it has same attributes as model except those which we want to hide eg.
    // createdAt, updatedAt, deletedAt
    private long id;
    private String name;
    private String description;
    private String imageUrl;
//    private String createdAt;
//    private String updatedAt;
//    private String deletedAt;
    private double price;
    private String category;

    public static ProductResponseDto fromProduct(Product product) {
        //After adding, negative test cases it was failing, so I should handle null cases
        if(product == null) {
            return null;
        }
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setImageUrl(product.getImageUrl());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setCategory(product.getCategory().getName());
        return productResponseDto;
    }
}
