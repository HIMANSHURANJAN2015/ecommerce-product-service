package com.scaler.ecommerceproductservice.services;

import com.scaler.ecommerceproductservice.dtos.FakeStoreProductDto;
import com.scaler.ecommerceproductservice.models.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
        FakeStoreProductDto fakeStoreProductDto = this.restTemplate.getForObject(
                "https://fakestoreapi.com/products/"+id,
                FakeStoreProductDto.class
        );
        return fakeStoreProductDto.toProduct();
    }
}
