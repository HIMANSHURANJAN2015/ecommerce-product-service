package com.scaler.ecommerceproductservice.controllers;

import com.scaler.ecommerceproductservice.dtos.ProductResponseDto;
import com.scaler.ecommerceproductservice.models.Product;
import com.scaler.ecommerceproductservice.services.FakeStoreProductService;
import com.scaler.ecommerceproductservice.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    //I need to call FakeStoreProductService again and again, so store it as global variable
    //To prevent tigh couploing b/w FakeStoreProductService and controller, I am using
    //productService type. thus fulfilling Liskov Sibstitution PRinciple
    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //To get details about particular product with given id
    //We have to give response back to client as ResponseEntity
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable long id) {
        Product product = this.productService.getProductById(id);
        //We need to return back DTO, not entire model
        ProductResponseDto productResponseDto = ProductResponseDto.fromProduct(product);
        return new ResponseEntity<>(productResponseDto,  HttpStatus.OK);
    }

    //to get list if all products
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<Product> products = this.productService.getAllProducts();
        List<ProductResponseDto> productResponseDtos = new ArrayList<ProductResponseDto>();
        for (Product product : products) {
            ProductResponseDto productResponseDto = ProductResponseDto.fromProduct(product);
            productResponseDtos.add(productResponseDto);
        }
        return new ResponseEntity<>(productResponseDtos, HttpStatus.OK);
    }

}
