package com.scaler.ecommerceproductservice.controllers;

import com.scaler.ecommerceproductservice.dtos.ProductResponseDto;
import com.scaler.ecommerceproductservice.exceptions.ProductNotFoundException;
import com.scaler.ecommerceproductservice.services.ProductAIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.scaler.ecommerceproductservice.models.Product;
import com.scaler.ecommerceproductservice.models.Category;
import com.scaler.ecommerceproductservice.services.ProductService;
import com.scaler.ecommerceproductservice.controllers.ProductController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductControllerTest {

    @MockitoBean
    //@Qualifier("productDBService")
    public ProductService productService;

    @MockitoBean
    public ProductAIService productAIService;

    @Autowired
    public ProductController productController;

    @Test
    public void testGetProductByIdReturnsProductResponseDto() throws ProductNotFoundException {
        //step-1: arrange
        Product dummyProduct = new Product();
        dummyProduct.setId(1L);
        dummyProduct.setName("name");
        dummyProduct.setDescription("description");
        dummyProduct.setPrice(12.7);
        dummyProduct.setImageUrl("img.url");

        Category dummyCategory = new Category();
        dummyCategory.setId(1L);
        dummyCategory.setName("category");
        dummyCategory.setDescription("description");

        dummyProduct.setCategory(dummyCategory);

        when(productService.getProductById(1L)).thenReturn(dummyProduct);

        //step-2: act
        ProductResponseDto productResponseDto
                = productController.getProductById(1L).getBody(); //getBody() since product controller is returning ResponseEntity

        //step-3: assert
        assertEquals(1L, productResponseDto.getId());
        assertEquals("name", productResponseDto.getName());
        assertEquals("description", productResponseDto.getDescription());
        assertEquals("img.url", productResponseDto.getImageUrl());
        assertEquals(12.7, productResponseDto.getPrice());
        assertEquals(1L, dummyCategory.getId());
    }

    @Test
    public void testGetProductByIdReturnsNull() throws ProductNotFoundException
    {
        //arrange
        when(productService.getProductById(1L)).thenReturn(null);

        //act
        ProductResponseDto productResponseDto = productController.getProductById(1L).getBody();

        //assert
        assertNull(productResponseDto);
    }

}
