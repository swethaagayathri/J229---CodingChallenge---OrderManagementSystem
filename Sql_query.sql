
CREATE DATABASE OMS;
USE OMS;
CREATE TABLE Product (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2),
    quantity_in_stock INT,
    type VARCHAR(20),
    brand VARCHAR(50),
    warranty_period INT,
    size VARCHAR(20),
    color VARCHAR(20)
);
CREATE TABLE Users (
    user_id INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL
);
CREATE TABLE Orders (
    order_id INT,
    user_id INT,
    product_id INT
 );

INSERT INTO Product (product_id, product_name, description, price, quantity_in_stock, type, brand, warranty_period, size, color)
VALUES 
    (1, 'Laptop', '15-inch laptop with SSD storage', 1200.00, 10, 'Electronics', 'Dell', 1, '15-inch', 'Silver'),
    (2, 'Smartphone', 'Latest smartphone with 5G capability', 800.00, 15, 'Electronics', 'Samsung', 1, '6.2-inch', 'Black');
  

INSERT INTO Users (user_id, username, password, role)
VALUES
    (100,'Swethaa','adm123','admin'),
    (102, 'Santhiya', 'password1', 'user');
 

INSERT INTO Orders (order_id, user_id, product_id)
VALUES
    (10000, 102, 1), 
    (10002, 102, 2);
