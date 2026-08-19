# 🔧 MYSQL DRIVER FIX - QUICK GUIDE

## ❌ Error You See
```
MySQL Driver not found!
ClassNotFoundException: com.mysql.jdbc.Driver
```

## ✅ Solution (Choose ONE)

---

### 🟢 **EASIEST WAY - Option 1**

**Just run this file:**
```
download_driver.bat
```

It will automatically download and install everything!

Then run:
```
run.bat
```

---

### 🟢 **QUICK WAY - Option 2**

Copy this command into PowerShell:

```powershell
Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.48/mysql-connector-java-5.1.48.jar' -OutFile 'lib\mysql-connector-java-5.1.48-bin.jar'
```

Then run:
```
run.bat
```

---

### 🟢 **MANUAL WAY - Option 3**

1. Download from: https://dev.mysql.com/downloads/connector/j/
2. Choose version: 5.1.48 (or any 5.1.x version)
3. Save to: `Canteen Management/lib/` folder
4. Run: `run.bat`

---

## ✅ Verify It Works

After downloading, you should have:
```
Canteen Management/
├── lib/
│   └── mysql-connector-java-5.1.48-bin.jar  ✅
├── run.bat
├── src/
├── bin/
└── ...
```

Then run:
```
run.bat
```

---

## 📋 Checklist

- [ ] Downloaded MySQL Connector JAR file
- [ ] Placed in `lib/` folder
- [ ] File exists: `lib/mysql-connector-java-5.1.48-bin.jar`
- [ ] MySQL Server is running
- [ ] Database 'cms' exists
- [ ] Ran: `run.bat`
- [ ] See login screen (no errors)
- [ ] Can login with admin@canteen.com / admin123

---

## 🎯 Files Updated

The following files have been updated to support the classpath:

✅ `run.bat` - Updated to include `lib/*` in classpath  
✅ `build.bat` - Updated to compile with MySQL driver  
✅ `download_driver.bat` - NEW - Auto-downloads driver  
✅ `MYSQL_DRIVER_FIX.md` - NEW - Detailed guide  

---

## 🚀 After Fix

Once fixed, run:

```bash
run.bat
```

You'll see:
- Professional login screen
- No error dialogs
- Ready to login!

**Default Login:**
- Email: `admin@canteen.com`
- Password: `admin123`

---

**That's it! Choose Option 1 for the easiest experience.** ✨
