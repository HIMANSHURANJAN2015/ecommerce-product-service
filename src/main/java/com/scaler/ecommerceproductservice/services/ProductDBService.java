package com.scaler.ecommerceproductservice.services;

import com.scaler.ecommerceproductservice.exceptions.ProductNotFoundException;
import com.scaler.ecommerceproductservice.models.Category;
import com.scaler.ecommerceproductservice.models.Product;
import com.scaler.ecommerceproductservice.repositories.CategoryRepository;
import com.scaler.ecommerceproductservice.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("productDBService")
public class ProductDBService implements ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public ProductDBService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProductById(long id){
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            return product.get();
        } else {
            throw new ProductNotFoundException("Product with id = " + id + " not found");
        }
    }

    @Override
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String name, String desc, double price, String imageURL, String category){
        Product product = new Product();
        product.setName(name);
        product.setDescription(desc);
        product.setPrice(price);
        product.setImageUrl(imageURL);

        Category categoryObj = getCategoryFromDB(category);
        product.setCategory(categoryObj);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(long id, String name, String desc, double price, String imageURL, String category){
        Product product = getProductById(id);
        product.setName(name);
        product.setDescription(desc);
        product.setPrice(price);
        product.setImageUrl(imageURL);
        Category categoryObj = getCategoryFromDB(category);
        product.setCategory(categoryObj);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(long id){
        //checking if product with given id exits, if not then throw error
        Product product = getProductById(id);

        productRepository.delete(product);
    }

    private Category getCategoryFromDB(String name){
        Optional<Category> categoryObj = categoryRepository.findByName(name);
        if(categoryObj.isPresent()){
            return categoryObj.get();
        }
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }
}
