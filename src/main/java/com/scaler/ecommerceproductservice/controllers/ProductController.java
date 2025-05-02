package com.scaler.ecommerceproductservice.controllers;

import com.scaler.ecommerceproductservice.dtos.ProductResponseDto;
import com.scaler.ecommerceproductservice.dtos.ProductPostRequestDto;
import com.scaler.ecommerceproductservice.dtos.ProductWithoutDescRequestDto;
import com.scaler.ecommerceproductservice.models.Product;
import com.scaler.ecommerceproductservice.services.ProductAIService;
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
    private final ProductAIService productAIService;

    //public ProductController(@Qualifier("productDBService") ProductService productService) {
    public ProductController(ProductService productService, ProductAIService productAIService) {
        this.productService = productService;
        this.productAIService = productAIService;
    }

    //To get details about particular product with given id
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable long id) {
        Product product = this.productService.getProductById(id);
        //We need to return back DTO, not entire model
        ProductResponseDto productResponseDto = ProductResponseDto.fromProduct(product);
        return new ResponseEntity<>(productResponseDto,  HttpStatus.OK);
    }

    //to get list if all products
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(@RequestParam(required = false) String category) {
        List<Product> products = null;
        if (category != null) {
            products = this.productService.getProductsByCategory(category);
        } else {
            products = this.productService.getAllProducts();
        }
        List<ProductResponseDto> productResponseDtos = new ArrayList<ProductResponseDto>();
        for (Product product : products) {
            ProductResponseDto productResponseDto = ProductResponseDto.fromProduct(product);
            productResponseDtos.add(productResponseDto);
        }
        return new ResponseEntity<>(productResponseDtos, HttpStatus.OK);
    }

    //to create a new product
    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductPostRequestDto productPostRequestDto) {
        Product p = this.productService.createProduct(productPostRequestDto.getName(),
                productPostRequestDto.getDescription(),
                productPostRequestDto.getPrice(),
                productPostRequestDto.getImageUrl(),
                productPostRequestDto.getCategory()
        );
        ProductResponseDto productResponseDto = ProductResponseDto.fromProduct(p);
        return new ResponseEntity<>(productResponseDto,  HttpStatus.CREATED);
    }

    @PostMapping("/products-without-description")
    public ProductResponseDto createProductWithAIDescription(
            @RequestBody ProductWithoutDescRequestDto productWithoutDescDto)
    {
        Product product = productAIService.createProductWithAIDescription(
                productWithoutDescDto.getName(),
                productWithoutDescDto.getPrice(),
                productWithoutDescDto.getImageUrl(),
                productWithoutDescDto.getCategory()
        );

        return ProductResponseDto.fromProduct(product);
    }

    //to update product
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestBody ProductPostRequestDto productPostRequestDto, @PathVariable long id) {
        Product p = this.productService.updateProduct(
                id,
                productPostRequestDto.getName(),
                productPostRequestDto.getDescription(),
                productPostRequestDto.getPrice(),
                productPostRequestDto.getImageUrl(),
                productPostRequestDto.getCategory()
        );
        ProductResponseDto productResponseDto = ProductResponseDto.fromProduct(p);
        return new ResponseEntity<>(productResponseDto,  HttpStatus.OK);
    }

    //to delete the product
    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        this.productService.deleteProduct(id);
        return ResponseEntity.ok("Product successfully deleted");
    }

    /*
    //M-2 of exceptional handler (With Response Entity)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException() {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage("Null pointer exception occurred");
        errorDto.setStatus("Failure");
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

     */


    /*
    //M-2 of exceptional handler (Without Response Entity)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NullPointerException.class)
    public ErrorDto handleNullPointerException() {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage("Null pointer exception occurred");
        errorDto.setStatus("Failure");
        return errorDto;
    }

     */
}