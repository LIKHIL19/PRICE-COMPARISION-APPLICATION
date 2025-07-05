@echo off
echo Compiling Java files...
javac -d out -cp "lib/mysql-connector-j-9.2.0/mysql-connector-j-9.2.0.jar" src/AppLauncher.java src/ui/*.java src/model/*.java src/db/*.java src/utils/*.java

if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Running application...
java -cp "out;lib/mysql-connector-j-9.2.0/mysql-connector-j-9.2.0.jar" AppLauncher

pause 