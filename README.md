# E-commerce-product-Order_Management_System-JDBC
E-commerce Product &amp; Order Management System **built with Java ,MYSQL and JDBC**, managing products, orders, and user authentication efficiently
**CLI-based application** for shopping & admin management and Provides Admin functionalities

## Project Structure

- Model: CartItem, Order, OrderItem, Payment, Product, User
- Repository: DAO (Data Access Object) layer handles database operations and it does this using JDBC (Java Database Connectivity)
### DAO Layer 
- The DAO layer separates the business logic from the database logic (queries, connections)
- Service: CartService, OrderService, PaymentService, ProductService, UserService
- Controller: The OrderController acts as a bridge between the user-facing logic and the business/service layer 
- Main: Provides console-based menu
- ConfigUtil: Loads db.properties

## Features

### User Management
- Register new users
- Existing users can log in using their ID
- Validation for new/existing users
### Product Management (Admin)
- Add, update, and delete products
- Manage product details (name, category, price, quantity)
- Admin credentials are read from `db.properties`
- View top-selling products and total revenue
### Cart Management
- Add items to cart by product ID
- View cart items based on user ID
- Remove specific items from cart
- Clear entire cart
- Cart persists per user
### Order Management
- Users can **checkout their cart** or **directly order a product**
- Handles both existing and new users efficiently
- Validates stock before placing an order
- Stores order history for each user
- Users can view their previous orders
### Reports & Analytics
- View all user orders by ID
- Admin can view:
  - Total revenue
  - Top-selling products
  - Product performance summaries
### Authentication
- Admin password stored securely in `db.properties`

## System Workflow

**User Registration / Login**
- New users can register with basic details
- Existing users provide their ID to continue shopping
 **Product Browsing**
- Lists all available products with price and stock details
 **Add to Cart**
- User adds desired products with quantity to their personal cart
- The system validates available stock
 **Order Placement**
- User can place orders directly or through cart checkout
- Order details are recorded with total amount and quantity
**Database Handling**
- DAO layer performs all database interactions using JDBC
- Database configuration is read from `db.properties`
 **Error Handling**
- Graceful error messages for invalid inputs, out-of-stock items, and unauthorized access

## Sample Run (CLI)

Oct 03, 2025 7:51:57 PM org.anudip.ecommerce.App main
INFO: E-commerce CLI demo started.
Choose an option:
Register user
Add product
List products
Add to cart
View cart
Remove from cart
Place order
List my orders
Update product
Delete product
View total revenue (admin)
Exit


## Database Tables Overview

| Table | Description |
|--------|--------------|
| users | Stores user details |
| products | Product catalog |
| cart | Temporary cart items for each user |
| orders | Order history with date, quantity, total |
| order_items | Individual product details per order |

## Technologies Used

| Component | Technology |
|------------|-------------|
| Language | Java |
| Database | MySQL |
| DB Access | JDBC |
| Design Pattern | DAO + MVC |
| Logging | java.util.logging |
| Config | db.properties |
| IDE | IntelliJ / Eclipse / VS Code |

## Future Improvements
- Add user authentication with login sessions
- Integrate payment gateway simulation
- GUI version using JavaFX or React + Spring Boot backend
  
# Conclusion 
- The E-Commerce Management System is a complete Java-based console application designed to simulate real-world online shopping operations. It demonstrates how different components interact within a structured layered architecture
- By integrating JDBC with MySQL, the system ensures reliable data storage and retrieval, while the modular design allows for easy extension

**Author**

Kamali Singaravelan
