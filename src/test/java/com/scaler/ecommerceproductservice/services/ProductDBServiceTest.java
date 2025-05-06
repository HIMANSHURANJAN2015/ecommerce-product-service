package com.scaler.ecommerceproductservice.services;

import com.scaler.ecommerceproductservice.exceptions.ProductNotFoundException;
import com.scaler.ecommerceproductservice.models.Category;
import com.scaler.ecommerceproductservice.models.Product;
import com.scaler.ecommerceproductservice.repositories.CategoryRepository;
import com.scaler.ecommerceproductservice.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductDBServiceTest {
    /*
        PRoductDB service has 3 dependencies, ProductRepository, CategoryRepository, and ChatClient.
        So, I need to mock all 3 methods
        Best Practices for Mocking Dependencies
            1. Mock all dependencies in the test class.
            2. Stub the behavior of each dependency to return expected values.
            3. Verify interactions to ensure the method calls are happening as expected.
     */
    @MockitoBean
    ChatClient chatClient;

    @MockitoBean
    ProductRepository productRepository;

    @MockitoBean
    CategoryRepository categoryRepository;

    @Autowired
    ProductDBService productDBService;

    @Test
    public void testGetProductByIdValidIdReturnsProduct() {
        //Arrange
        Product dummyProduct = new Product();
        dummyProduct.setId(1L);
        dummyProduct.setName("Be young cargo");
        dummyProduct.setDescription("Beautiful cargo with relax fit");
        dummyProduct.setPrice(12.7);
        dummyProduct.setImageUrl("img.url");

        Category dummyCategory = new Category();
        dummyCategory.setId(1L);
        dummyCategory.setName("category");
        dummyCategory.setDescription("description");

        dummyProduct.setCategory(dummyCategory);
        when(productRepository.findById(1L)).thenReturn(Optional.of(dummyProduct));
        //act
        Product product = productDBService.getProductById(1L);
        //assert
        assertEquals(1L, product.getId());
        assertEquals("Be young cargo", product.getName());
    }

    @Test
    public void testGetProductByIdInvalidIdThrowsException() {
        //arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        //act and assert
        assertThrows(ProductNotFoundException.class, () -> productDBService.getProductById(1L));

    }

    @Test
    public void testCreateProductWithAIDescriptionWithValidDetailsReturnsProduct() {
        /*
        createProduct() has nothing to test if we are omcking repository.
        So, I am not writing test cases of it but writing test cases for AI deswcription
         */
        // step-1: Arrange

        // Mock category retrieval
        String categoryName = "Men's joggers";
        Category dummyCategory = new Category();
        dummyCategory.setId(1L);
        dummyCategory.setName(categoryName);
        dummyCategory.setDescription("Collection of stylish and sturdy mens joggers");
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(dummyCategory));

        // Mock AI-generated description
        String mockDescription = "Beautiful cargo with relax fit";
        /*
            In service, this is the method call.
                chatClient.prompt().user(prompt).call().content();
            when(chatClient.prompt().user(anyString()).call().content()).thenReturn(mockDescription); wont work
            since, each method in the chain should return a valid mock object.
            so I am breaking like this.
         */
        ChatClient.ChatClientRequestSpec mockRequestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec mockCallResponseSpec = mock(ChatClient.CallResponseSpec.class);

        when(chatClient.prompt()).thenReturn(mockRequestSpec);
        when(mockRequestSpec.user(anyString())).thenReturn(mockRequestSpec);
        when(mockRequestSpec.call()).thenReturn(mockCallResponseSpec); // Correct return type
        when(mockCallResponseSpec.content()).thenReturn(mockDescription);

        //Mock Product saving
        String productName = "Be young cargo joggers - olive";
        double price = 1200;
        String imageUrl = "img.url";
        Product dummyProduct = new Product();
        dummyProduct.setId(1L);
        dummyProduct.setName(productName);
        dummyProduct.setDescription(mockDescription);
        dummyProduct.setPrice(price);
        dummyProduct.setImageUrl(imageUrl);
        dummyProduct.setCategory(dummyCategory);
        when(productRepository.save(any(Product.class))).thenReturn(dummyProduct);

        //Step-2 act
        Product createdProduct = productDBService.createProductWithAIDescription(productName, price, imageUrl, categoryName);

        //step-3 : assert
        assertNotNull(createdProduct);
        assertEquals(productName, createdProduct.getName());
        assertEquals(price, createdProduct.getPrice());
        assertEquals(imageUrl, createdProduct.getImageUrl());
        assertEquals(dummyCategory, createdProduct.getCategory());
        assertEquals(mockDescription, createdProduct.getDescription());

        // Verify interactions
        verify(categoryRepository).findByName(categoryName);
        //verify(chatClient).prompt().user(anyString()).call().content();
        verify(productRepository).save(any(Product.class));
    }
}