# Canteen Management System - Configuration Guide

## Database Setup

### Prerequisites
- MySQL Server 5.6 or higher
- MySQL Connector/J (JDBC Driver) - should be in the lib folder

### Step 1: Create Database

Open MySQL Command Line or MySQL Workbench and run:

```sql
-- Run the database_setup.sql file
source database_setup.sql;
```

Or manually create the tables:

```sql
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

-- Stored Procedures
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

-- Insert sample data
INSERT INTO user (user_name, user_email, user_phone, user_password) VALUES 
('Admin', 'admin@canteen.com', 9999999999, 'admin123'),
('Sameer', 'sameer@email.com', 9876543210, 'pass123');

INSERT INTO item (item_name, item_price, item_qty) VALUES 
('Dosa', 80, 100),
('Idli', 40, 150),
('Vada', 30, 120);
```

### Step 2: Verify Database Connection

Test the connection by running:

```sql
USE cms;
SHOW TABLES;
SELECT * FROM user;
```

## Java Configuration

### Check Java Version

```bash
java -version
```

Ensure Java 8 or higher is installed.

### Compile Project

**Windows:**
```bash
cd src
javac -encoding UTF-8 -d ../bin *.java Admin/*.java dao/*.java Data/*.java service/*.java
```

**Linux/Mac:**
```bash
cd src
javac -encoding UTF-8 -d ../bin *.java Admin/*.java dao/*.java Data/*.java service/*.java
```

### Run Application

**Windows (using batch file):**
```bash
run.bat
```

**Linux/Mac (using shell script):**
```bash
bash run.sh
```

**Direct Java command:**
```bash
cd bin
java loginpage
```

## Database Connection Settings

Edit `src/dao/Connection.java` if your MySQL settings are different:

```java
private static final String DRIVER = "com.mysql.jdbc.Driver";
private static final String URL = "jdbc:mysql://localhost:3306/cms";
private static final String USER = "root";
private static final String PASSWORD = "root";
```

Change these values to match your MySQL installation:
- **DRIVER**: MySQL JDBC Driver (usually stays the same)
- **URL**: Change localhost or 3306 if your MySQL server runs on different host/port
- **USER**: Your MySQL username
- **PASSWORD**: Your MySQL password

## Troubleshooting

### Issue: "Connection refused"
- Check if MySQL server is running
- Verify hostname and port in Connection.java
- Check firewall settings

### Issue: "Access denied for user"
- Verify MySQL username and password
- Check if user has permissions for 'cms' database

### Issue: "Database not found"
- Run the database_setup.sql file
- Verify database 'cms' exists: `SHOW DATABASES;`

### Issue: "Class not found"
- Ensure MySQL Connector/J JAR is in classpath
- Check compilation output for import errors

## Default Credentials

### Admin Account
- Email: `admin@canteen.com`
- Password: `admin123`
- Access: Full admin panel with user/item/order management

### Test User Account
- Email: `sameer@email.com`
- Password: `pass123`
- Access: Regular user features

## Important Notes

1. **Change Default Passwords**: Update passwords after first login
2. **Backup Database**: Regular backups recommended
3. **Port Availability**: Default MySQL port is 3306
4. **Firewall**: Ensure MySQL port is not blocked
5. **Java Path**: Ensure Java bin directory is in PATH environment variable

## Quick Start

1. Ensure MySQL is running
2. Run database_setup.sql to create tables
3. Compile: `javac -encoding UTF-8 -d ../bin *.java Admin/*.java dao/*.java Data/*.java service/*.java`
4. Run: `java loginpage`

## Support

For issues:
1. Check error messages in console
2. Verify database connection
3. Check logs for details
4. Ensure all prerequisites are installed

---

**Version**: 1.0  
**Last Updated**: January 2026
