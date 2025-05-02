package com.scaler.ecommerceproductservice.services;

import com.scaler.ecommerceproductservice.exceptions.ProductNotFoundException;
import com.scaler.ecommerceproductservice.models.Category;
import com.scaler.ecommerceproductservice.models.Product;
import com.scaler.ecommerceproductservice.repositories.CategoryRepository;
import com.scaler.ecommerceproductservice.repositories.ProductRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@Service("productDBService")
@Service
@Profile("default")
public class ProductDBService implements ProductService, ProductAIService {
    private final ChatClient chatClient;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;

    public ProductDBService(ProductRepository productRepository, CategoryRepository categoryRepository, ChatClient chatClient) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.chatClient = chatClient;
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
    public List<Product> getProductsByCategory(String categoryName){
        //m-1 using named query "(most optimal)
        //return productRepository.findByCategory_Name(categoryName);
        //m-2 using HQL
        //return productRepository.findProductsByCategoryName(categoryName);
        //M-3 using named query
        return productRepository.findProductsByCategoryNameNative(categoryName);
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
    public Product createProductWithAIDescription(String name, double price, String imageUrl, String category) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setImageUrl(imageUrl);

        Category categoryObj = getCategoryFromDB(category);
        product.setCategory(categoryObj);

        String description = getDescriptionFromAI(product);
        product.setDescription(description);

        return productRepository.save(product);
    }

    private String getDescriptionFromAI(Product product)
    {
        String prompt = String.format(
                "Generate a 150-word professional marketing description for a %s product named '%s'. " +
                        "Key features: Priced at $%.2f, Category: %s. " +
                        "Focus on benefits and unique selling points. Avoid technical jargon. Use markdown formatting.",
                product.getCategory().getName().toLowerCase(),
                product.getName(),
                product.getPrice(),
                product.getCategory().getName()
        );

        return chatClient.prompt().user(prompt).call().content();
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
