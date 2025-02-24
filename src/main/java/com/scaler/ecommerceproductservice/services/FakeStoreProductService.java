package com.scaler.ecommerceproductservice.services;

import com.scaler.ecommerceproductservice.dtos.FakeStoreProductGetDto;
import com.scaler.ecommerceproductservice.dtos.FakeStoreProductPostDto;
import com.scaler.ecommerceproductservice.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FakeStoreProductService implements ProductService {

    //To call 3rd party API, we use RestTemplate
    //To inject it we need to it as Bean, but since its 3rd party, hence we need to create
    //configs class
    RestTemplate restTemplate;

    public FakeStoreProductService(RestTemplate apiClient){
        this.restTemplate = apiClient;
    }

    @Override
    public Product getProductById(long id) {
        //FakeStore product should not be dumbed directly to our product, so we create
        //FakeStoreProductDto.java
        FakeStoreProductGetDto fakeStoreProductGetDto = this.restTemplate.getForObject(
                "https://fakestoreapi.com/products/"+id,
                FakeStoreProductGetDto.class
        );
        return fakeStoreProductGetDto.toProduct();
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductGetDto[] fakeStoreProductGetDtos = this.restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                FakeStoreProductGetDto[].class
        );
        List<Product> products = new ArrayList<>();
        for (FakeStoreProductGetDto fakeStoreProductGetDto : fakeStoreProductGetDtos) {
            Product product = fakeStoreProductGetDto.toProduct();
            products.add(product);
        }
        return products;
    }

    @Override
    public Product createProduct(String name, String desc, double price, String imageURL, String category) {
        //createing FakeStoreProductPostDto object
        FakeStoreProductPostDto fakeStoreProductPostDto = new FakeStoreProductPostDto();
        fakeStoreProductPostDto.setTitle(name);
        fakeStoreProductPostDto.setDescription(desc);
        fakeStoreProductPostDto.setPrice(price);
        fakeStoreProductPostDto.setImage(imageURL);
        fakeStoreProductPostDto.setCategory(category);
        //sending create request and getting response
        FakeStoreProductGetDto fakeStoreProductGetDto = this.restTemplate.postForObject(
                "https://fakestoreapi.com/products",
                fakeStoreProductPostDto,
                FakeStoreProductGetDto.class
        );
        return fakeStoreProductGetDto.toProduct();
    }
}
