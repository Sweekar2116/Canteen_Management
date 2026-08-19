#!/bin/bash

# Canteen Management System Startup Script for Linux/Mac

echo ""
echo "==================================="
echo "Canteen Management System"
echo "==================================="
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Java is not installed or not in PATH"
    echo "Please install Java JDK 8 or higher"
    exit 1
fi

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$SCRIPT_DIR"

# Check if MySQL is running
if ! command -v mysql &> /dev/null; then
    echo "MySQL client not found. Please ensure MySQL server is running."
fi

# Compile the project if bin directory doesn't exist
if [ ! -d "bin" ]; then
    echo "Compiling project..."
    cd src
    javac -encoding UTF-8 -d ../bin *.java Admin/*.java dao/*.java Data/*.java service/*.java
    if [ $? -ne 0 ]; then
        echo "Compilation failed. Please check Java installation."
        exit 1
    fi
    cd ..
fi

# Run the application
echo "Starting Canteen Management System..."
echo ""
cd bin
java loginpage

if [ $? -ne 0 ]; then
    echo ""
    echo "Application failed to start"
    echo "Please ensure:"
    echo "1. MySQL server is running (mysql -u root -p)"
    echo "2. Database 'cms' exists"
    echo "3. Database credentials are correct (root/root)"
    exit 1
fi
