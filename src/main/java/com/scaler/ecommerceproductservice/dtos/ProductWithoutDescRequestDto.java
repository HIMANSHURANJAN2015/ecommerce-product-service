package com.scaler.ecommerceproductservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductWithoutDescRequestDto {
    private String name;
    private double price;
    private String imageUrl;
    private String category;
}