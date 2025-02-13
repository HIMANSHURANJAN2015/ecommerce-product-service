package com.scaler.ecommerceproductservice.dtos;

import com.scaler.ecommerceproductservice.models.Category;
import com.scaler.ecommerceproductservice.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductDto {

    //it has same attributes as response from FakesSToreAPI, except those which we dont need
    private long id;
    private String title; //KIM: its "name" in our model, but in response of fakestore its "title".
    private double price;
    private String category;
    private String description;
    private String image;

    //To convert DTO to product in service and pass on the response back to controller
    public Product toProduct() {
        Product product = new Product();
        product.setId(id);
        product.setName(title);
        product.setDescription(description);
        product.setImageUrl(image);
        product.setPrice(price);

        Category category1 = new Category();
        category1.setName(category);
        product.setCategory(category1);
        return product;
    }
}
