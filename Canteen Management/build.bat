@echo off
REM Build script for Canteen Management System

setlocal enabledelayedexpansion

echo.
echo ===================================
echo Canteen Management System - Build
echo ===================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java JDK 8 or higher
    pause
    exit /b 1
)

REM Check if MySQL driver exists
if not exist "lib\mysql-connector-java-5.1.48-bin.jar" (
    echo.
    echo ========================================
    echo WARNING: MySQL Driver Missing
    echo ========================================
    echo.
    echo The MySQL JDBC driver is required to run this application.
    echo.
    echo Download Options:
    echo 1. Download from: https://dev.mysql.com/downloads/connector/j/
    echo    Save as: lib/mysql-connector-java-5.1.48-bin.jar
    echo.
    echo 2. Or download using PowerShell:
    echo    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.48/mysql-connector-java-5.1.48.jar' -OutFile 'lib/mysql-connector-java-5.1.48-bin.jar'"
    echo.
    pause
)

REM Navigate to source directory
cd /d "%~dp0\src"

echo Compiling Java files...
echo.

REM Compile all Java files with MySQL driver in classpath
javac -encoding UTF-8 -cp "../lib/*" -d ../bin *.java Admin\*.java dao\*.java Data\*.java service\*.java util\*.java 2>build_errors.log

if errorlevel 1 (
    echo.
    echo ===================================
    echo COMPILATION FAILED
    echo ===================================
    echo.
    echo Errors:
    type build_errors.log
    echo.
    echo Check src\build_errors.log for details
    pause
    exit /b 1
) else (
    echo.
    echo ===================================
    echo BUILD SUCCESSFUL!
    echo ===================================
    echo.
    echo Compiled classes are in: bin\
    echo.
    echo To run the application:
    echo 1. cd bin
    echo 2. java -cp ".;../lib/*" loginpage
    echo.
    echo Or use: run.bat
    pause
)
