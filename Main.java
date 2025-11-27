1.Folder Structure




EcommercePlatform/
│── src/
│   ├── database/
│   │     └── DBConnection.java
│   ├── models/
│   │     ├── User.java
│   │     ├── Product.java
│   │     └── Order.java
│   ├── dao/
│   │     ├── UserDAO.java
│   │     ├── ProductDAO.java
│   │     └── OrderDAO.java
│   ├── services/
│   │     ├── AdminService.java
│   │     ├── SellerService.java
│   │     └── BuyerService.java
│   └── Main.java
│
│── database.sql
│── README.md
│── .gitignore






2. Source Code




src/database/DBConnection.java

package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/ecommerce";
    private static final String USER = "root"; 
    private static final String PASSWORD = "root"; 

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

src/models/User.java

package models;

public class User {
    public int id;
    public String name;
    public String email;
    public String role;

    public User(int id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}


src/models/Product.java

package models;

public class Product {
    public int id;
    public int sellerId;
    public String name;
    public String description;
    public double price;
    public int stock;

    public Product(int id, int sellerId, String name, String description, double price, int stock) {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }
}

src/models/Order.java

package models;

public class Order {
    public int id;
    public int buyerId;
    public int productId;
    public int quantity;
    public String status;

    public Order(int id, int buyerId, int productId, int quantity, String status) {
        this.id = id;
        this.buyerId = buyerId;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
    }
}





DAO LAYER (CRUD Operations)

src/dao/UserDAO.java

package dao;

import database.DBConnection;
import models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserDAO {

    public void addUser(User user) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO users(name, email, role) VALUES (?, ?, ?)"
            );
            ps.setString(1, user.name);
            ps.setString(2, user.email);
            ps.setString(3, user.role);
            ps.executeUpdate();
            System.out.println("User added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


src/dao/ProductDAO.java

package dao;

import database.DBConnection;
import models.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProductDAO {

    public void addProduct(Product product) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO products(seller_id, name, description, price, stock) VALUES (?, ?, ?, ?, ?)"
            );
            ps.setInt(1, product.sellerId);
            ps.setString(2, product.name);
            ps.setString(3, product.description);
            ps.setDouble(4, product.price);
            ps.setInt(5, product.stock);
            ps.executeUpdate();
            System.out.println("Product added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


src/dao/OrderDAO.java

package dao;

import database.DBConnection;
import models.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class OrderDAO {

    public void placeOrder(Order order) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO orders(buyer_id, product_id, quantity, status) VALUES (?, ?, ?, ?)"
            );
            ps.setInt(1, order.buyerId);
            ps.setInt(2, order.productId);
            ps.setInt(3, order.quantity);
            ps.setString(4, order.status);
            ps.executeUpdate();
            System.out.println("Order placed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





SERVICE LAYER

src/services/AdminService.java

package services;

import dao.UserDAO;
import models.User;

public class AdminService {

    UserDAO userDAO = new UserDAO();

    public void createUser(String name, String email, String role) {
        userDAO.addUser(new User(0, name, email, role));
    }
}


src/services/SellerService.java

package services;

import dao.ProductDAO;
import models.Product;

public class SellerService {

    ProductDAO productDAO = new ProductDAO();

    public void addProduct(int sellerId, String name, String description, double price, int stock) {
        productDAO.addProduct(new Product(0, sellerId, name, description, price, stock));
    }
}


src/services/BuyerService.java

package services;

import dao.OrderDAO;
import models.Order;

public class BuyerService {

    OrderDAO orderDAO = new OrderDAO();

    public void purchaseProduct(int buyerId, int productId, int quantity) {
        orderDAO.placeOrder(new Order(0, buyerId, productId, quantity, "Placed"));
    }
}







MAIN EXECUTION FILE

src/Main.java

import services.AdminService;
import services.SellerService;
import services.BuyerService;

public class Main {

    public static void main(String[] args) {

        AdminService admin = new AdminService();
        SellerService seller = new SellerService();
        BuyerService buyer = new BuyerService();

        System.out.println("=== E-COMMERCE PLATFORM RUNNING ===");

        admin.createUser("Utsav", "utsav@mail.com", "Seller");
        admin.createUser("Amit", "amit@mail.com", "Buyer");

        seller.addProduct(1, "Laptop", "Core i5 Processor", 55000, 10);

        buyer.purchaseProduct(2, 1, 1);

        System.out.println("=== END ===");
    }
}





3. database.sql


CREATE DATABASE ecommerce;
USE ecommerce;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    role VARCHAR(20)
);

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    seller_id INT,
    name VARCHAR(100),
    description TEXT,
    price DOUBLE,
    stock INT,
    FOREIGN KEY (seller_id) REFERENCES users(id)
);

CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    buyer_id INT,
    product_id INT,
    quantity INT,
    status VARCHAR(50),
    FOREIGN KEY (buyer_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);






4. README.md


# Online E-Commerce Platform (Java + JDBC + MySQL)

## Overview
A backend e-commerce platform built using Core Java, OOP, JDBC, and MySQL.

## Features
### Admin
- Manage users
- Manage all products
- Manage orders

### Seller
- Add product listings
- Manage inventory
- Process orders

### Buyer
- Browse products
- Purchase items
- Track orders

## Technologies Used
- Java
- JDBC
- MySQL
- DAO Architecture
- OOP Principles

## How to Run
1. Install MySQL and create database using `database.sql`
2. Update DBConnection.java with your MySQL username/password
3. Import project into IntelliJ/Eclipse
4. Run `Main.java`

## Project Structure
See `/src` for full code.

## Author
Utkarsh Saxena
