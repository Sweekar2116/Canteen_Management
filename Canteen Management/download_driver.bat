@echo off
REM Download MySQL JDBC Driver

echo.
echo ========================================
echo Downloading MySQL Connector/J
echo ========================================
echo.

setlocal enabledelayedexpansion

REM Create lib folder if it doesn't exist
if not exist "lib" mkdir lib

echo Downloading mysql-connector-java-5.1.48-bin.jar...
echo This may take a few moments...
echo.

REM Use PowerShell to download the file
powershell -Command "try { Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.48/mysql-connector-java-5.1.48.jar' -OutFile 'lib\mysql-connector-java-5.1.48-bin.jar' -ErrorAction Stop; Write-Host 'Download completed successfully!'; Write-Host 'File saved to: lib\mysql-connector-java-5.1.48-bin.jar' } catch { Write-Host 'Download failed. Please download manually from: https://dev.mysql.com/downloads/connector/j/'; Write-Host 'Save the jar file to: lib\mysql-connector-java-5.1.48-bin.jar' }"

echo.
echo ========================================
echo Done! You can now run: run.bat
echo ========================================
echo.

pause
