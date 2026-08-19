@echo off
REM Canteen Management System Startup Script

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo Java is not installed or not in PATH
    echo Please install Java JDK 8 or higher
    pause
    exit /b 1
)

REM Set the working directory
cd /d "%~dp0"

REM Compile the project (optional)
echo.
echo ===================================
echo Canteen Management System
echo ===================================
echo.

REM Check if bin directory exists
if not exist "bin" (
    echo Compiling project...
    cd src
    javac -encoding UTF-8 -d ../bin *.java Admin/*.java dao/*.java Data/*.java service/*.java util/*.java
    if errorlevel 1 (
        echo Compilation failed. Please check Java installation and database connection.
        pause
        exit /b 1
    )
    cd ..
)

REM Check if MySQL driver exists
if not exist "lib\mysql-connector-java-5.1.48-bin.jar" (
    echo.
    echo ========================================
    echo MySQL Driver Missing!
    echo ========================================
    echo.
    echo Please download MySQL Connector/J:
    echo 1. Visit: https://dev.mysql.com/downloads/connector/j/
    echo 2. Download: mysql-connector-java-5.1.48-bin.jar
    echo 3. Place it in the 'lib' folder
    echo.
    echo Or use this command:
    echo PowerShell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.48/mysql-connector-java-5.1.48.jar' -OutFile 'lib/mysql-connector-java-5.1.48-bin.jar'"
    echo.
    pause
    exit /b 1
)

REM Run the application with MySQL driver in classpath
echo Starting Canteen Management System...
echo.
cd bin
java -cp ".;../lib/*" loginpage

if errorlevel 1 (
    echo.
    echo Application failed to start
    echo Please ensure:
    echo 1. MySQL server is running
    echo 2. Database 'cms' exists
    echo 3. Database credentials are correct (root with no password)
    pause
)
