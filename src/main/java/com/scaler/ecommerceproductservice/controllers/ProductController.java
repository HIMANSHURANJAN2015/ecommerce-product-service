package com.scaler.ecommerceproductservice.controllers;

import com.scaler.ecommerceproductservice.dtos.ProductResponseDto;
import com.scaler.ecommerceproductservice.models.Product;
import com.scaler.ecommerceproductservice.services.FakeStoreProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    //I need to call FakeStoreProductService again and again, so store it as global variable
    FakeStoreProductService fakeStoreProductService;

    public ProductController(FakeStoreProductService fakeStoreProductService) {
        this.fakeStoreProductService = fakeStoreProductService;
    }

    //We have to give response back to client as ResponseEntity
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable long id) {
        Product product = fakeStoreProductService.getProductById(id);
        //We need to return back DTO, not entire model
        ProductResponseDto productResponseDto = ProductResponseDto.fromProduct(product);
        return new ResponseEntity<>(productResponseDto,  HttpStatus.FORBIDDEN);
    }

}
