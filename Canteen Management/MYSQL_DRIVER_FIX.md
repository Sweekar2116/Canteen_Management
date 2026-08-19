# MYSQL DRIVER SETUP GUIDE

## Problem
The application shows: **"MySQL Driver not found! Please ensure MySQL connector is installed."**

This means the MySQL JDBC driver (JAR file) is not in the classpath.

---

## Solution: Option 1 (Automatic - Easiest)

### Step 1: Run the download script
Double-click: **download_driver.bat**

This will automatically download and install the MySQL connector.

### Step 2: Run the application
Once download is complete, run: **run.bat**

---

## Solution: Option 2 (Manual Download)

### Step 1: Download MySQL Connector/J
1. Visit: https://dev.mysql.com/downloads/connector/j/
2. Select version: 5.1.48 (or latest)
3. Download: mysql-connector-java-5.1.48-bin.jar

### Step 2: Place the file
1. Create a folder named **lib** in the project root (if not exists)
2. Copy the downloaded JAR file into the **lib** folder
3. The path should be: `Canteen Management/lib/mysql-connector-java-5.1.48-bin.jar`

### Step 3: Run the application
Execute: **run.bat**

---

## Solution: Option 3 (PowerShell Command)

Open PowerShell in the project directory and run:

```powershell
Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.48/mysql-connector-java-5.1.48.jar' -OutFile 'lib\mysql-connector-java-5.1.48-bin.jar'
```

Then run: **run.bat**

---

## Verification

After setup, verify the file exists:
```
Canteen Management/
└── lib/
    └── mysql-connector-java-5.1.48-bin.jar  ✅
```

---

## Still Getting Error?

1. **Ensure MySQL is running**
   - Open MySQL Command Line: `mysql -u root -p`
   - Enter your password

2. **Verify database exists**
   - In MySQL: `SHOW DATABASES;`
   - Should see: **cms**

3. **Create database if missing**
   - Run: `source database_setup.sql;`

4. **Check driver file**
   - Ensure: `lib/mysql-connector-java-5.1.48-bin.jar` exists

---

## Quick Fix Checklist

- [ ] MySQL Server is running
- [ ] MySQL Connector/J is downloaded
- [ ] JAR file is in `lib/` folder
- [ ] Database 'cms' exists
- [ ] Run build.bat to recompile
- [ ] Run run.bat to start application

---

## After Fix: Expected Login Screen

Once fixed, you should see:
- Clean login form
- "User Login" title
- Email and Password fields
- Sign Up button
- Forgot Password button
- No error dialogs

**Default Credentials:**
- Email: admin@canteen.com
- Password: admin123

---

For any issues, refer to CONFIGURATION.md or COMPLETE_GUIDE.md
