package com.scaler.ecommerceproductservice.services;

import com.scaler.ecommerceproductservice.dtos.FakeStoreProductResponseDto;
import com.scaler.ecommerceproductservice.dtos.FakeStoreProductPostRequestDto;
import com.scaler.ecommerceproductservice.exceptions.ProductNotFoundException;
import com.scaler.ecommerceproductservice.models.Product;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service("fakeStoreProductService")
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
        FakeStoreProductResponseDto fakeStoreProductResponseDto = this.restTemplate.getForObject(
                "https://fakestoreapi.com/products/"+id,
                FakeStoreProductResponseDto.class
        );
        if(fakeStoreProductResponseDto ==null) {
            throw new ProductNotFoundException("Product with id "+id+" not found");
        }

        /*
        //M-1 of exceptional handler(Default exception handler)
        if(fakeStoreProductResponseDto == null) {
            throw new NoSuchElementException("No product found with id"+id);
        }
        */

        /* using custom exception with m-1(Default exception handler)
        if(fakeStoreProductResponseDto == null) {
            throw new ProductNotFoundException("No product found with id"+id);
        }

         */

        return fakeStoreProductResponseDto.toProduct();
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductResponseDto[] fakeStoreProductResponseDtos = this.restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                FakeStoreProductResponseDto[].class
        );
        List<Product> products = new ArrayList<>();
        for (FakeStoreProductResponseDto fakeStoreProductResponseDto : fakeStoreProductResponseDtos) {
            Product product = fakeStoreProductResponseDto.toProduct();
            products.add(product);
        }
        return products;
    }

    @Override
    public Product createProduct(String name, String desc, double price, String imageURL, String category) {
        //createing FakeStoreProductPostDto object
        FakeStoreProductPostRequestDto fakeStoreProductPostRequestDto = new FakeStoreProductPostRequestDto();
        fakeStoreProductPostRequestDto.setTitle(name);
        fakeStoreProductPostRequestDto.setDescription(desc);
        fakeStoreProductPostRequestDto.setPrice(price);
        fakeStoreProductPostRequestDto.setImage(imageURL);
        fakeStoreProductPostRequestDto.setCategory(category);
        //sending create request and getting response
        FakeStoreProductResponseDto fakeStoreProductResponseDto = this.restTemplate.postForObject(
                "https://fakestoreapi.com/products",
                fakeStoreProductPostRequestDto,
                FakeStoreProductResponseDto.class
        );
        return fakeStoreProductResponseDto.toProduct();
    }

    @Override
    public Product updateProduct(long id, String name, String desc, double price, String imageURL, String category){
        //Checking if product exist. If not then throws error
        getProductById(id);

        //Using FakeStoreProductPostRequestDto for update also. Creating postrequestdto
        FakeStoreProductPostRequestDto fakeStoreProductPostRequestDto = new FakeStoreProductPostRequestDto();
        fakeStoreProductPostRequestDto.setTitle(name);
        fakeStoreProductPostRequestDto.setDescription(desc);
        fakeStoreProductPostRequestDto.setPrice(price);
        fakeStoreProductPostRequestDto.setImage(imageURL);
        fakeStoreProductPostRequestDto.setCategory(category);
        //Creating Http request entity
        //HttpEntity<FakeStoreProductPostRequestDto> requestEntity = new HttpEntity<>(fakeStoreProductPostRequestDto);
        //Making the request
        ResponseEntity<FakeStoreProductResponseDto> responseEntity = this.restTemplate.exchange(
                "https://fakestoreapi.com/products/"+id,
                HttpMethod.PUT,
                new HttpEntity<FakeStoreProductPostRequestDto>(fakeStoreProductPostRequestDto),
               FakeStoreProductResponseDto.class
        );
        return responseEntity.getBody().toProduct();
    }

    @Override
    public void deleteProduct(long id){
        //Checking if product exist. If not then throws error
        getProductById(id);

        this.restTemplate.delete("https://fakestoreapi.com/products/"+id);
    }
}
