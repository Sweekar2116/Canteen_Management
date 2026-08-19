-- Canteen Management System Database Setup
-- Run this script in MySQL to initialize the database

CREATE DATABASE IF NOT EXISTS cms;
USE cms;

-- User Table
CREATE TABLE IF NOT EXISTS user (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(100) NOT NULL,
  user_email VARCHAR(100) UNIQUE NOT NULL,
  user_phone BIGINT NOT NULL,
  user_password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Item Table
CREATE TABLE IF NOT EXISTS item (
  item_id INT AUTO_INCREMENT PRIMARY KEY,
  item_name VARCHAR(100) NOT NULL UNIQUE,
  item_price INT NOT NULL,
  item_qty INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Order Details Table
CREATE TABLE IF NOT EXISTS order_details (
  order_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  order_date DATE NOT NULL,
  order_status INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- Cart Table
CREATE TABLE IF NOT EXISTS cart (
  cart_id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT NOT NULL,
  item_id INT NOT NULL,
  item_name VARCHAR(100) NOT NULL,
  item_qty INT NOT NULL,
  FOREIGN KEY (order_id) REFERENCES order_details(order_id) ON DELETE CASCADE,
  FOREIGN KEY (item_id) REFERENCES item(item_id) ON DELETE CASCADE
);

-- Payment Table
CREATE TABLE IF NOT EXISTS payment (
  payment_id INT AUTO_INCREMENT PRIMARY KEY,
  bill_date DATE NOT NULL,
  order_id INT NOT NULL,
  user_id INT NOT NULL,
  total INT NOT NULL,
  payment_status INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (order_id) REFERENCES order_details(order_id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- Sample Data
INSERT INTO user (user_name, user_email, user_phone, user_password) VALUES 
('Admin', 'admin@canteen.com', 9999999999, 'admin123'),
('Sameer', 'sameer@email.com', 9876543210, 'pass123');

INSERT INTO item (item_name, item_price, item_qty) VALUES 
('Dosa', 80, 100),
('Idli', 40, 150),
('Vada', 30, 120);

-- Create Stored Procedures
DELIMITER //

CREATE PROCEDURE IF NOT EXISTS getUsers()
BEGIN
  SELECT user_id, user_name, user_email, user_phone, user_password FROM user;
END//

CREATE PROCEDURE IF NOT EXISTS getOrder()
BEGIN
  SELECT order_id, user_id, order_date FROM order_details WHERE order_status = 0;
END//

DELIMITER ;

-- Display confirmation
SELECT 'Database setup completed successfully!' AS Status;
