package com.scaler.ecommerceproductservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductPostRequestDto {
    //Attributes are based on POST request format by FakeStoreAPI, except those which we dont need.
    private String title;
    private double price;
    private String description;
    private String image;
    private String category;
}
