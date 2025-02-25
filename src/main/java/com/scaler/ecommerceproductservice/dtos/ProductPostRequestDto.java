package com.scaler.ecommerceproductservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPostRequestDto {
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;
}
