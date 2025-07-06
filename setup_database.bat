@echo off
echo Setting up MySQL database...

REM Check if MySQL is installed and running
mysql --version >nul 2>&1
if errorlevel 1 (
    echo MySQL is not installed or not in PATH
    echo Please install MySQL and try again
    pause
    exit /b 1
)

REM Drop existing database and create new one
echo Dropping existing database...
mysql -u root -pLikhil@1976 -e "DROP DATABASE IF EXISTS price_comparison;"

REM Create database and tables
echo Creating database and tables...
mysql -u root -pLikhil@1976 < sql/schema.sql

if errorlevel 1 (
    echo Failed to create database and tables
    pause
    exit /b 1
)

echo Database setup completed successfully!
pause 