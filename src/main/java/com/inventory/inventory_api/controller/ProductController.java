package com.inventory.inventory_api.controller;

import com.inventory.inventory_api.repository.ProductRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.inventory.inventory_api.entity.Product;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll(); // Goes through database to find all instead of storing into a list, and returning list
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (productRepository.existsBySku(product.getSku())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Product savedProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setSku(productDetails.getSku());
                    product.setPriceInCents(productDetails.getPriceInCents());
                    product.setQuantity(productDetails.getQuantity());
                    product.setCategory(productDetails.getCategory());

                    Product updatedProduct = productRepository.save(product);
                    return ResponseEntity.ok(updatedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }



}
