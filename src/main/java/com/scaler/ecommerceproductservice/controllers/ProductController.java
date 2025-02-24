package com.scaler.ecommerceproductservice.controllers;

import com.scaler.ecommerceproductservice.dtos.ProductGetDto;
import com.scaler.ecommerceproductservice.dtos.ProductPostDto;
import com.scaler.ecommerceproductservice.models.Product;
import com.scaler.ecommerceproductservice.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductGetDto> getProductById(@PathVariable long id) {
        Product product = this.productService.getProductById(id);
        //We need to return back DTO, not entire model
        ProductGetDto productGetDto = ProductGetDto.fromProduct(product);
        return new ResponseEntity<>(productGetDto,  HttpStatus.OK);
    }

    //to get list if all products
    @GetMapping("/products")
    public ResponseEntity<List<ProductGetDto>> getAllProducts() {
        List<Product> products = this.productService.getAllProducts();
        List<ProductGetDto> productGetDtos = new ArrayList<ProductGetDto>();
        for (Product product : products) {
            ProductGetDto productGetDto = ProductGetDto.fromProduct(product);
            productGetDtos.add(productGetDto);
        }
        return new ResponseEntity<>(productGetDtos, HttpStatus.OK);
    }

    //to create a new product
    @PostMapping("/products")
    public ResponseEntity<ProductGetDto> createProduct(@RequestBody ProductPostDto productPostDto) {
        Product p = this.productService.createProduct(productPostDto.getName(),
                productPostDto.getDescription(),
                productPostDto.getPrice(),
                productPostDto.getImageUrl(),
                productPostDto.getCategory()
        );
        ProductGetDto productGetDto = ProductGetDto.fromProduct(p);
        return new ResponseEntity<>(productGetDto,  HttpStatus.CREATED);
    }
}