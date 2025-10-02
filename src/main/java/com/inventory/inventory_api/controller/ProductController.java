package com.inventory.inventory_api.controller;

import com.inventory.inventory_api.exception.ProductNotFoundException;
import com.inventory.inventory_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.inventory.inventory_api.entity.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts(@RequestParam(required = false) String category) {
        if (category != null) {
            return productRepository.findByCategory(category.toUpperCase());
        }
        return productRepository.findAll(); // Goes through database to find all instead of storing into a list, and returning list
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @GetMapping("/low-stock")
    public List<Product> getLowStockItems(@RequestParam(defaultValue = "5") Integer threshold) {
        return productRepository.findByQuantityLessThanAndDepletingTrue(threshold);
    }

    @GetMapping("/search")
    public Product getProductBySku(@RequestParam String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));
    }

    @GetMapping("/stats/total-value")
    public Map<String, Object> getTotalInventoryValue() {
        List<Product> allProducts = productRepository.findAll();

        long totalValue = allProducts.stream()
                .mapToLong(p -> (long) p.getQuantity() * p.getPriceInCents())
                .sum();

        long totalQuantity = allProducts.stream()
                .mapToInt(p -> p.getQuantity())
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("TotalValueCents", totalValue);
        stats.put("TotalValueDollars", totalValue / 100.0);
        stats.put("TotalProducts", allProducts.size());
        stats.put("TotalQuantity", totalQuantity);

        return stats;
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
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

            product.setName(productDetails.getName());
            product.setSku(productDetails.getSku());
            product.setPriceInCents(productDetails.getPriceInCents());
            product.setQuantity(productDetails.getQuantity());
            product.setCategory(productDetails.getCategory());
            product.setDepleting(productDetails.getDepleting());

            return productRepository.save(product);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
       Product product = productRepository.findById(id)
               .orElseThrow(() -> new ProductNotFoundException(id));
       productRepository.delete(product);
       return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/adjust-stock")
    public Product adjustStock(@PathVariable Long id, @RequestParam Integer amount) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        int newQuantity = product.getQuantity() + amount;

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Cannot reduce stock below 0. Current: " + product.getQuantity() + ", Attempted adjustment: " + amount);
        }

        product.setQuantity(newQuantity);
        return productRepository.save(product);
    }

}
