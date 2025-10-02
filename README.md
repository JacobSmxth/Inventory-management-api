# Inventory Management API

A RESTful inventory management system built with Java and Spring Boot. Track products, manage stock levels, and monitor inventory value with a clean API interface.

## Features

- **Full CRUD Operations** - Create, read, update, and delete products
- **Search & Filter** - Find products by SKU or filter by category
- **Stock Management** - Adjust quantities with validation to prevent negative stock
- **Low Stock Tracking** - Monitor consumable items that need restocking
- **Inventory Statistics** - Calculate total value and quantity across all products
- **SKU Validation** - Enforce proper SKU formatting (e.g., LOGI-G502H)
- **Custom Error Handling** - Clear, descriptive error messages for all failure cases

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA** - Database abstraction layer
- **Spring Validation** - Input validation
- **H2 Database** - File-based persistence (PostgreSQL migration planned)

## Getting Started

### Prerequisites

- Java 21 or higher
- Gradle

### Installation

1. Clone the repository
```bash
git clone https://github.com/JacobSmxth/inventory-management-api.git
cd inventory-management-api
```

2. Run the application
```bash
./gradlew bootRun
```

The API will start on http://localhost:8080

## API Endpoints

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/items` | Get all products (optional: `?category=DISPLAY`) |
| GET | `/api/items/{id}` | Get product by ID |
| GET | `/api/items/search?sku={sku}` | Find product by SKU |
| POST | `/api/items` | Create new product |
| PUT | `/api/items/{id}` | Update product |
| DELETE | `/api/items/{id}` | Delete product |

### Stock Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| PATCH | `/api/items/{id}/adjust-stock?amount={amount}` | Adjust stock quantity |
| GET | `/api/items/low-stock?threshold={number}` | Get depleting items below threshold |

### Statistics

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/items/stats/total-value` | Get total inventory value and item count |


## Example Requests

### Create a Product
```bash
curl -X POST http://localhost:8080/api/items \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gaming Mouse",
    "sku": "LOGI-G502H",
    "priceInCents": 7999,
    "quantity": 5,
    "category": "PERIPHERALS",
    "depleting": false
  }'
```

### Adjust Stock
```bash
curl -X PATCH "http://localhost:8080/api/items/1/adjust-stock?amount=-2"
```

### Get Inventory stats
```bash
curl http://localhost:8080/api/items/stats/total-value
```

Response:
```
{
  "TotalValueCents": 141116,
  "TotalValueDollars": 1411.16,
  "TotalItems": 9,
  "TotalQuantity": 19
}
```

## Product Schema
```
{
  "id": 1,
  "name": "Gaming Mouse",
  "sku": "LOGI-G502H",
  "priceInCents": 7999,
  "quantity": 5,
  "category": "PERIPHERALS",
  "depleting": false,
  "createdAt": "2025-10-02T12:01:17.612023",
  "updatedAt": "2025-10-02T12:01:17.612032"
}
```

## License
MIT

