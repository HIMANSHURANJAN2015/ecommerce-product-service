package com.scaler.ecommerceproductservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product extends BaseModel {

    @Column(length = 10000)
    private String description;

    private String imageUrl;
    private double price;

    @ManyToOne
    private Category category;
}
