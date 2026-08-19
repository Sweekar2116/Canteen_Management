# Canteen Management System - Complete Setup & User Guide

## Overview

The Canteen Management System (CMS) is a comprehensive Java Swing application designed to streamline canteen operations including user management, menu management, order processing, and payment tracking.

## What's Been Improved

### ✅ Bug Fixes
- Removed all problematic imports (com.mysql.cj.x.protobuf, com.mysql.cj.exceptions)
- Fixed ArrayList initialization using diamond operator
- Modernized code with lambda expressions
- Added proper error handling with user-friendly messages
- Fixed NullPointerException risks
- Removed printStackTrace calls, replaced with JOptionPane error dialogs

### ✅ Database Improvements
- Enhanced Connection.java with better error handling
- Added database connection validation
- Proper connection closing to avoid resource leaks
- Clear error messages for database issues
- Created comprehensive database setup script

### ✅ UI/UX Enhancements
- Modern color scheme (professional blue and dark colors)
- Improved button styling with hover effects
- Better text field tooltips
- Enhanced error messages
- Support for Enter key in login
- Responsive button feedback

### ✅ Code Quality
- Converted anonymous classes to lambda expressions
- Added @Override annotations where needed
- Better exception handling
- Consistent naming conventions
- Added JOptionPane for user feedback

### ✅ Documentation
- Created README.md with complete feature list
- Created CONFIGURATION.md with detailed setup steps
- Created this comprehensive guide
- Added database_setup.sql for easy setup
- Created run.bat and run.sh for easy startup

## Quick Start Guide

### For Windows Users

#### Step 1: Prerequisites
1. Download and install [Java JDK 8 or higher](https://www.oracle.com/java/technologies/downloads/)
2. Download and install [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
3. Download [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)

#### Step 2: Database Setup
1. Open MySQL Command Line Client or MySQL Workbench
2. Create database:
```sql
CREATE DATABASE cms;
```
3. Run the provided database_setup.sql:
```bash
mysql -u root -p cms < database_setup.sql
```

#### Step 3: Compile Project
```bash
cd "Canteen Management"
build.bat
```

#### Step 4: Run Application
```bash
run.bat
```

### For Linux/Mac Users

#### Step 1: Prerequisites
```bash
# Install Java
sudo apt-get install default-jdk

# Install MySQL
sudo apt-get install mysql-server

# Verify installations
java -version
mysql --version
```

#### Step 2: Database Setup
```bash
# Create database
mysql -u root -p -e "CREATE DATABASE cms;"

# Run setup script
mysql -u root -p cms < database_setup.sql
```

#### Step 3: Compile Project
```bash
cd "Canteen Management"
chmod +x build.sh
./build.sh
```

#### Step 4: Run Application
```bash
chmod +x run.sh
./run.sh
```

## Features Overview

### User Features
1. **Registration** - Create account with email and phone
2. **Login** - Secure authentication
3. **Browse Menu** - View all available items with prices
4. **Order Items** - Add to cart and place orders
5. **Track Orders** - View order status and history
6. **Payment** - View and manage payments
7. **Password Reset** - Forgot password recovery

### Admin Features
1. **Dashboard** - Real-time statistics
   - Total users registered
   - Total menu items
   - Pending orders count
   
2. **User Management**
   - View all registered users
   - User details (email, phone)
   - Delete users if needed
   
3. **Item Management**
   - View all menu items
   - Add new items
   - Edit prices and quantities
   - Delete items
   
4. **Order Management**
   - View pending orders
   - View order details
   - Mark orders as completed
   - View order history
   
5. **Payment Management**
   - Track all payments
   - View payment details by date
   - Generate payment reports
   - View transaction history

## Default Test Accounts

### Admin Account (Full Access)
```
Email: admin@canteen.com
Password: admin123
```

### Regular User Account
```
Email: sameer@email.com
Password: pass123
```

**Important**: Change these passwords after first login!

## Database Schema

### Tables Created

1. **user**
   - user_id (Primary Key)
   - user_name
   - user_email (Unique)
   - user_phone
   - user_password
   - created_at

2. **item**
   - item_id (Primary Key)
   - item_name (Unique)
   - item_price
   - item_qty
   - created_at

3. **order_details**
   - order_id (Primary Key)
   - user_id (Foreign Key)
   - order_date
   - order_status (0=pending, 1=completed)
   - created_at

4. **cart**
   - cart_id (Primary Key)
   - order_id (Foreign Key)
   - item_id (Foreign Key)
   - item_name
   - item_qty

5. **payment**
   - payment_id (Primary Key)
   - bill_date
   - order_id (Foreign Key)
   - user_id (Foreign Key)
   - total
   - payment_status
   - created_at

## Troubleshooting

### Common Issues and Solutions

#### Issue: "Database connection failed"
```
Solution:
1. Ensure MySQL server is running
2. Check if database 'cms' exists
3. Verify username (root) and password are correct
4. Check firewall isn't blocking port 3306
```

#### Issue: "MySQL Driver not found"
```
Solution:
1. Ensure mysql-connector-java JAR is in classpath
2. Copy JAR to a lib folder if needed
3. Update classpath in build command
```

#### Issue: "Table doesn't exist"
```
Solution:
1. Run database_setup.sql again
2. Check if database was created: SHOW DATABASES;
3. Check tables exist: USE cms; SHOW TABLES;
```

#### Issue: "Connection refused"
```
Solution:
1. Check if MySQL server is running
2. Verify host and port (default: localhost:3306)
3. Check firewall settings
4. Restart MySQL service
```

#### Issue: "Access denied for user 'root'@'localhost'"
```
Solution:
1. Check MySQL password is correct
2. Verify user exists and has permissions
3. Reset MySQL password if needed
4. Update Connection.java with correct credentials
```

## Configuration

### Database Configuration
Edit `src/dao/Connection.java` if needed:

```java
private static final String DRIVER = "com.mysql.jdbc.Driver";
private static final String URL = "jdbc:mysql://localhost:3306/cms";
private static final String USER = "root";
private static final String PASSWORD = "root";
```

### Port Configuration
If MySQL runs on different port:
```java
private static final String URL = "jdbc:mysql://localhost:3307/cms"; // Change 3307 to your port
```

### Host Configuration
If MySQL is on different machine:
```java
private static final String URL = "jdbc:mysql://192.168.1.100:3306/cms"; // Change IP address
```

## Project Structure

```
Canteen Management/
├── src/                          # Source code
│   ├── Adminwelcome.java         # Admin dashboard
│   ├── loginpage.java            # Login screen
│   ├── signuppage.java           # Registration screen
│   ├── Welcome.java              # User dashboard
│   ├── menu.java                 # Menu browsing
│   ├── CartDetails.java          # Shopping cart
│   ├── CheckOrder.java           # Order tracking
│   ├── CheckPayment.java         # Payment view
│   ├── ForgotPassword.java       # Password reset
│   ├── LoginAdmin.java           # Admin login
│   ├── orderstatus.java          # Order status
│   ├── Admin/                    # Admin-specific classes
│   │   ├── ItemTable.java
│   │   ├── UserTable.java
│   │   ├── OrderTable.java
│   │   └── PaymentTable.java
│   ├── dao/                      # Database access
│   │   └── Connection.java       # DB operations
│   ├── Data/                     # Data models
│   │   ├── User.java
│   │   ├── Item.java
│   │   ├── OrderDetails.java
│   │   ├── Cart.java
│   │   └── Payment.java
│   └── service/                  # Business logic
│       ├── LoginService.java     # Auth & user ops
│       ├── OrderService.java     # Order ops
│       └── priceService.java     # Price calculations
├── bin/                          # Compiled classes
├── img/                          # Images
├── database_setup.sql            # DB initialization
├── CONFIGURATION.md              # Setup guide
├── README.md                     # Features guide
├── build.bat                     # Windows build script
├── run.bat                       # Windows run script
└── run.sh                        # Linux/Mac run script
```

## Keyboard Shortcuts

| Key | Action |
|-----|--------|
| Enter | Submit form / Login |
| Tab | Move to next field |
| Ctrl+Q | Exit application |
| Esc | Go back / Close window |

## Performance Tips

1. **Database Indexing**: Indexes are on email and order dates for faster queries
2. **Connection Pooling**: Consider implementing connection pooling for better performance
3. **Caching**: Menu items could be cached for frequently viewed items
4. **Query Optimization**: Stored procedures are used for complex queries

## Security Recommendations

1. **Change Default Passwords**: Update admin credentials immediately
2. **Use HTTPS**: Implement SSL/TLS if extending to web
3. **Input Validation**: All inputs are validated before database operations
4. **SQL Injection Prevention**: Prepared statements prevent SQL injection
5. **Database Backup**: Regular backups recommended (daily/weekly)

## Future Enhancements

- [ ] Email notifications for orders
- [ ] SMS alerts for order status
- [ ] Mobile app version
- [ ] Online payment integration
- [ ] Analytics and reporting dashboard
- [ ] Multi-language support
- [ ] Dark theme option
- [ ] Receipt printing
- [ ] Inventory alerts
- [ ] Employee management

## Support & Help

### Getting Help
1. Check CONFIGURATION.md for setup issues
2. Check README.md for feature documentation
3. Review database_setup.sql for schema reference
4. Check console output for error messages

### Common Questions

**Q: Can I use a different database?**
A: Yes, but you'll need to modify Connection.java and update the JDBC driver accordingly.

**Q: How do I backup the database?**
A: Use `mysqldump -u root -p cms > backup.sql`

**Q: Can multiple users use the system simultaneously?**
A: Yes, MySQL handles concurrent connections.

**Q: How do I reset a user's password?**
A: Admin can use Forgot Password feature or directly update database.

## Version History

**Version 1.0 (Current)**
- Initial release
- Core features implemented
- Database integration complete
- UI improvements completed
- Error handling enhanced
- Documentation added

## License

This project is for educational purposes.

## Contributors

- Development Team
- UI/UX Improvements
- Database Design
- Documentation

---

**Last Updated**: January 2026  
**Support Email**: support@canteenms.local  
**Documentation Version**: 1.0

---

## Checklist Before Running

- [ ] Java JDK 8+ installed and verified
- [ ] MySQL Server installed and running
- [ ] MySQL database 'cms' created
- [ ] Tables created from database_setup.sql
- [ ] MySQL Connector/J in classpath
- [ ] Project compiled successfully
- [ ] No compilation errors
- [ ] Database connection tested
- [ ] Default accounts verified

If all items are checked, you're ready to run: `java loginpage`

Happy Coding! 🚀
