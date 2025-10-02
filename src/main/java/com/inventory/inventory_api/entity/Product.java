package com.inventory.inventory_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "SKU is required")
    @Pattern(regexp = "^[A-Z]{3,6}-[A-Z0-9]{2,6}$", message = "SKU must match the format: XXX-XXX (e.g, LOGI-G502")
    @Column(nullable = false, unique = true)
    private String sku;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price can't be negative")
    @Column(nullable = false)
    private Integer priceInCents;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity can't be negative")
    @Column(nullable = false)
    private Integer quantity;

    @Column
    private String category;

    @Column
    private boolean depleting;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Product() {}

    public Product(String name, String sku, Integer priceInCents, Integer quantity, String category, boolean depleting) {
        this.name = name;
        this.sku = sku;
        this.priceInCents = priceInCents;
        this.quantity = quantity;
        this.category = category;
        this.depleting = depleting;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getPriceInCents() {
        return priceInCents;
    }
    public void setPriceInCents(Integer priceInCents) {
        this.priceInCents = priceInCents;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category.toUpperCase();
    }

    public boolean getDepleting() {
        return depleting;
    }
    public void setDepleting(boolean depleting) {
        this.depleting = depleting;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
