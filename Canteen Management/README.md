# Canteen Management System (CMS)

A Java Swing-based desktop application for managing a canteen's menu, orders, users, and payments.

## Features

- **User Management**
  - User registration and login
  - Password reset functionality
  - User profile management

- **Admin Dashboard**
  - View all registered users
  - Manage menu items (add, edit, delete)
  - Track orders
  - View payment records
  - Dashboard with statistics (user count, items, pending orders)

- **Order Management**
  - Browse menu items
  - Add items to cart
  - Place orders
  - Check order status
  - View order history

- **Payment System**
  - Process payments
  - View payment history
  - Generate bills

## System Requirements

- **Java**: JDK 8 or higher
- **MySQL**: Version 5.6 or higher
- **MySQL Connector/J**: Version 5.1.48 or higher
- **RAM**: Minimum 2GB
- **Disk Space**: 500MB

## Installation Steps

### 1. Install MySQL and Create Database

```bash
# Option A: Using MySQL Command Line
mysql -u root -p
# Enter your MySQL password, then run:

CREATE DATABASE IF NOT EXISTS cms;
USE cms;

# Create tables (use the provided database_setup.sql file)
source path/to/database_setup.sql;
```

**OR**

```bash
# Option B: Using MySQL Workbench
1. Open MySQL Workbench
2. Execute the database_setup.sql file
```

### 2. Configure Database Connection

Edit `src/dao/Connection.java` if needed:

```java
private static final String URL = "jdbc:mysql://localhost:3306/cms";
private static final String USER = "root";
private static final String PASSWORD = "root";
```

Change the password if your MySQL root password is different.

### 3. Compile the Project

```bash
cd "Canteen Management/src"
javac -encoding UTF-8 -d ../bin *.java Admin/*.java dao/*.java Data/*.java service/*.java
```

### 4. Run the Application

```bash
cd ../bin
java loginpage
```

## Database Setup

The database schema includes:

- **user**: Stores user account information
- **item**: Menu items with prices and quantities
- **order_details**: Customer orders
- **cart**: Items in orders
- **payment**: Payment records

## Default Login Credentials

- **Admin Login**:
  - Email: `admin@canteen.com`
  - Password: `admin123`

- **User Login**:
  - Email: `sameer@email.com`
  - Password: `pass123`

## Project Structure

```
Canteen Management/
├── src/
│   ├── *.java                 # Main UI classes
│   ├── Admin/                 # Admin interface classes
│   ├── dao/                   # Database access classes
│   ├── Data/                  # Data model classes
│   └── service/               # Business logic classes
├── bin/                       # Compiled class files
├── img/                       # Image resources
└── database_setup.sql         # Database initialization script
```

## Main Classes

### UI Classes
- `loginpage.java` - User login interface
- `signuppage.java` - User registration
- `Welcome.java` - User dashboard
- `Adminwelcome.java` - Admin dashboard
- `menu.java` - Menu browsing interface

### Data Classes
- `User.java` - User data model
- `Item.java` - Menu item data model
- `Order.java` - Order data model
- `Cart.java` - Shopping cart data model
- `Payment.java` - Payment data model

### Service Classes
- `LoginService.java` - Authentication and user operations
- `OrderService.java` - Order management
- `priceService.java` - Price calculations

### DAO Classes
- `Connection.java` - Database connection and CRUD operations

## Features in Detail

### User Features
1. **Registration**: Create new account with email and phone
2. **Login**: Secure login with email and password
3. **Browse Menu**: View available items
4. **Place Order**: Add items to cart and checkout
5. **Track Order**: View order status and history
6. **Payments**: View bills and payment status

### Admin Features
1. **Dashboard**: View statistics (users, items, pending orders)
2. **User Management**: View and delete users
3. **Item Management**: Add, edit, delete menu items and manage inventory
4. **Order Management**: View pending and completed orders
5. **Payment Tracking**: View daily payments and generate reports

## Troubleshooting

### Issue: "Database connection failed"
**Solution**: 
1. Ensure MySQL server is running
2. Check if database "cms" exists
3. Verify username and password in Connection.java
4. Check if port 3306 is available

### Issue: "MySQL Driver not found"
**Solution**:
1. Ensure mysql-connector-java-5.1.48-bin.jar is in the classpath
2. Copy the jar file to a lib folder and update compilation command

### Issue: "No valid tables found"
**Solution**:
1. Run the database_setup.sql file to create tables
2. Verify database connection
3. Check if tables were created using: `SHOW TABLES;` in MySQL

## Keyboard Shortcuts

- `ESC` - Go back / Exit window
- `Tab` - Navigate between fields
- `Enter` - Submit form / Login

## Security Notes

1. **Change Default Password**: Change the default admin password after first login
2. **Database Security**: Use strong MySQL passwords
3. **Data Backup**: Regular backup of MySQL database recommended

## Future Enhancements

- Email notifications for orders
- SMS alerts
- Mobile app version
- Online payment integration
- Analytics and reports
- Multi-language support
- Dark theme

## Support

For issues or questions, please check:
1. Database connection settings
2. MySQL server status
3. Java version compatibility
4. Required jar files in classpath

## License

This project is for educational purposes.

## Contributors

- Development Team

---

**Version**: 1.0  
**Last Updated**: January 2026
