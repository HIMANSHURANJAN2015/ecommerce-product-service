package com.scaler.ecommerceproductservice.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private long id; //using long for id, since its generally very hue
    private String name;
    private String description;
    private String imageUrl;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
    private double price;
    private Category category;
}
