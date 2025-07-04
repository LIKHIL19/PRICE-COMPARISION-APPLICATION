-- Create database
CREATE DATABASE IF NOT EXISTS price_comparison;
USE price_comparison;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Product prices from different platforms
CREATE TABLE IF NOT EXISTS product_prices (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    platform VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    url VARCHAR(255) NOT NULL,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Wishlist table
CREATE TABLE IF NOT EXISTS wishlists (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    UNIQUE KEY unique_wishlist_item (user_id, product_id)
);

-- Insert sample products (Smartphones)
INSERT INTO products (name, description, category) VALUES
('Samsung Galaxy M14 5G', '6GB RAM, 128GB Storage, 5G Smartphone', 'Smartphones'),
('Redmi Note 13 5G', '6GB RAM, 128GB Storage, 5G Smartphone', 'Smartphones'),
('Nothing Phone (2a)', '8GB RAM, 128GB Storage, Glyph Interface', 'Smartphones'),
('Samsung Galaxy S23 FE', '8GB RAM, 128GB Storage, Flagship Camera', 'Smartphones'),
('OnePlus Nord CE4', '8GB RAM, 128GB Storage, 5G Smartphone', 'Smartphones'),
('iPhone 15', '128GB Storage, A16 Bionic Chip', 'Smartphones'),
('Xiaomi 14', '12GB RAM, 256GB Storage, Flagship Camera', 'Smartphones'),
('Google Pixel 8', '8GB RAM, 128GB Storage, AI Camera', 'Smartphones'),
('iQOO Neo 9 Pro', '8GB RAM, 128GB Storage, Gaming Phone', 'Smartphones'),
('Realme 12 Pro+ 5G', '8GB RAM, 128GB Storage, Premium Camera', 'Smartphones');

-- Insert sample products (Earbuds)
INSERT INTO products (name, description, category) VALUES
('boAt Airdopes 141', 'Wireless Earbuds with 42H Playtime', 'Earbuds'),
('realme Buds Air 3 Neo', 'Active Noise Cancellation, 30H Battery', 'Earbuds'),
('Sony WF-1000XM5', 'Premium Noise Cancelling Earbuds', 'Earbuds'),
('Apple AirPods (3rd Gen)', 'Spatial Audio, MagSafe Charging', 'Earbuds'),
('Nothing Ear (2)', 'Active Noise Cancellation, 36H Battery', 'Earbuds'),
('OnePlus Buds Pro 2', 'Active Noise Cancellation, 39H Battery', 'Earbuds'),
('boAt Airdopes 175', 'Wireless Earbuds with 40H Playtime', 'Earbuds'),
('Sennheiser Momentum True Wireless 4', 'Premium Sound, ANC, 30H Battery', 'Earbuds'),
('Noise Buds VS102 Pro', 'Wireless Earbuds with 40H Playtime', 'Earbuds'),
('Realme Buds Air 5 Pro', 'Active Noise Cancellation, 40H Battery', 'Earbuds');

-- Insert sample products (Laptops)
INSERT INTO products (name, description, category) VALUES
('HP 15s (Ryzen 5)', '15.6-inch, 8GB RAM, 512GB SSD', 'Laptops'),
('Lenovo IdeaPad Slim 3', '15.6-inch, Intel Core i5, 8GB RAM, 512GB SSD', 'Laptops'),
('Apple MacBook Air 13-inch', 'M3 chip, 8GB RAM, 256GB SSD', 'Laptops'),
('Dell Inspiron 15', '15.6-inch, Intel Core i7, 16GB RAM, 512GB SSD', 'Laptops'),
('ASUS Zenbook 14 OLED', '14-inch OLED, Latest Gen, 16GB RAM, 512GB SSD', 'Laptops'),
('Acer Aspire 5', '15.6-inch, Intel Core i5, 8GB RAM, 512GB SSD', 'Laptops'),
('Microsoft Surface Laptop Studio 2', '14.4-inch, Intel Core i7, 32GB RAM, 1TB SSD', 'Laptops'),
('HP Pavilion Aero 13', '13.3-inch, Ryzen 7, 16GB RAM, 512GB SSD', 'Laptops'),
('Samsung Galaxy Book3 Ultra', '16-inch, Intel Core i7, 32GB RAM, 1TB SSD', 'Laptops'),
('Lenovo Legion 5', '15.6-inch, Ryzen 7, 16GB RAM, 512GB SSD, RTX 3060', 'Laptops');

-- Insert smartphone prices
INSERT INTO product_prices (product_id, platform, price, url) VALUES
(1, 'Amazon', 12499.00, 'https://amazon.in/samsung-galaxy-m14'),
(1, 'Flipkart', 11999.00, 'https://flipkart.com/samsung-galaxy-m14'),
(1, 'Croma', 12999.00, 'https://croma.com/samsung-galaxy-m14'),
(2, 'Amazon', 17999.00, 'https://amazon.in/redmi-note13'),
(2, 'Flipkart', 18499.00, 'https://flipkart.com/redmi-note13'),
(2, 'Croma', 17499.00, 'https://croma.com/redmi-note13'),
(3, 'Amazon', 22999.00, 'https://amazon.in/nothing-phone2a'),
(3, 'Flipkart', 24499.00, 'https://flipkart.com/nothing-phone2a'),
(3, 'Croma', 23499.00, 'https://croma.com/nothing-phone2a'),
(4, 'Amazon', 44999.00, 'https://amazon.in/samsung-galaxy-s23fe'),
(4, 'Flipkart', 43999.00, 'https://flipkart.com/samsung-galaxy-s23fe'),
(4, 'Croma', 45499.00, 'https://croma.com/samsung-galaxy-s23fe'),
(5, 'Amazon', 23999.00, 'https://amazon.in/oneplus-nord-ce4'),
(5, 'Flipkart', 24499.00, 'https://flipkart.com/oneplus-nord-ce4'),
(5, 'Croma', 25499.00, 'https://croma.com/oneplus-nord-ce4'),
(6, 'Amazon', 62499.00, 'https://amazon.in/iphone15'),
(6, 'Flipkart', 61749.00, 'https://flipkart.com/iphone15'),
(6, 'Croma', 62999.00, 'https://croma.com/iphone15'),
(7, 'Amazon', 68999.00, 'https://amazon.in/xiaomi14'),
(7, 'Flipkart', 70499.00, 'https://flipkart.com/xiaomi14'),
(7, 'Croma', 69999.00, 'https://croma.com/xiaomi14'),
(8, 'Amazon', 70999.00, 'https://amazon.in/pixel8'),
(8, 'Flipkart', 69999.00, 'https://flipkart.com/pixel8'),
(8, 'Croma', 68999.00, 'https://croma.com/pixel8'),
(9, 'Amazon', 34499.00, 'https://amazon.in/iqoo-neo9pro'),
(9, 'Flipkart', 35999.00, 'https://flipkart.com/iqoo-neo9pro'),
(9, 'Croma', 33999.00, 'https://croma.com/iqoo-neo9pro'),
(10, 'Amazon', 27999.00, 'https://amazon.in/realme12pro'),
(10, 'Flipkart', 28499.00, 'https://flipkart.com/realme12pro'),
(10, 'Croma', 27499.00, 'https://croma.com/realme12pro');

-- Insert earbuds prices
INSERT INTO product_prices (product_id, platform, price, url) VALUES
(11, 'Amazon', 1299.00, 'https://amazon.in/boat-airdopes141'),
(11, 'Flipkart', 1199.00, 'https://flipkart.com/boat-airdopes141'),
(11, 'Croma', 1399.00, 'https://croma.com/boat-airdopes141'),
(12, 'Amazon', 2499.00, 'https://amazon.in/realme-budsair3neo'),
(12, 'Flipkart', 2299.00, 'https://flipkart.com/realme-budsair3neo'),
(12, 'Croma', 2599.00, 'https://croma.com/realme-budsair3neo'),
(13, 'Amazon', 23990.00, 'https://amazon.in/sony-wf1000xm5'),
(13, 'Flipkart', 25490.00, 'https://flipkart.com/sony-wf1000xm5'),
(13, 'Croma', 24490.00, 'https://croma.com/sony-wf1000xm5'),
(14, 'Amazon', 18499.00, 'https://amazon.in/airpods3'),
(14, 'Flipkart', 17999.00, 'https://flipkart.com/airpods3'),
(14, 'Croma', 18999.00, 'https://croma.com/airpods3'),
(15, 'Amazon', 9999.00, 'https://amazon.in/nothing-ear2'),
(15, 'Flipkart', 8999.00, 'https://flipkart.com/nothing-ear2'),
(15, 'Croma', 9499.00, 'https://croma.com/nothing-ear2'),
(16, 'Amazon', 10499.00, 'https://amazon.in/oneplus-budspro2'),
(16, 'Flipkart', 11499.00, 'https://flipkart.com/oneplus-budspro2'),
(16, 'Croma', 9999.00, 'https://croma.com/oneplus-budspro2'),
(17, 'Amazon', 1799.00, 'https://amazon.in/boat-airdopes175'),
(17, 'Flipkart', 1699.00, 'https://flipkart.com/boat-airdopes175'),
(17, 'Croma', 1899.00, 'https://croma.com/boat-airdopes175'),
(18, 'Amazon', 27990.00, 'https://amazon.in/sennheiser-mtw4'),
(18, 'Flipkart', 26990.00, 'https://flipkart.com/sennheiser-mtw4'),
(18, 'Croma', 27490.00, 'https://croma.com/sennheiser-mtw4'),
(19, 'Amazon', 1499.00, 'https://amazon.in/noise-vs102pro'),
(19, 'Flipkart', 1399.00, 'https://flipkart.com/noise-vs102pro'),
(19, 'Croma', 1599.00, 'https://croma.com/noise-vs102pro'),
(20, 'Amazon', 6499.00, 'https://amazon.in/realme-budsair5pro'),
(20, 'Flipkart', 7499.00, 'https://flipkart.com/realme-budsair5pro'),
(20, 'Croma', 5999.00, 'https://croma.com/realme-budsair5pro');

-- Insert laptop prices
INSERT INTO product_prices (product_id, platform, price, url) VALUES
(21, 'Amazon', 49999.00, 'https://amazon.in/hp15s-ryzen5'),
(21, 'Flipkart', 48999.00, 'https://flipkart.com/hp15s-ryzen5'),
(21, 'Croma', 50499.00, 'https://croma.com/hp15s-ryzen5'),
(22, 'Amazon', 52999.00, 'https://amazon.in/ideapad-slim3'),
(22, 'Flipkart', 51999.00, 'https://flipkart.com/ideapad-slim3'),
(22, 'Croma', 53499.00, 'https://croma.com/ideapad-slim3'),
(23, 'Amazon', 113900.00, 'https://amazon.in/macbook-air-m3'),
(23, 'Flipkart', 115400.00, 'https://flipkart.com/macbook-air-m3'),
(23, 'Croma', 114400.00, 'https://croma.com/macbook-air-m3'),
(24, 'Amazon', 74999.00, 'https://amazon.in/dell-inspiron15'),
(24, 'Flipkart', 73999.00, 'https://flipkart.com/dell-inspiron15'),
(24, 'Croma', 75499.00, 'https://croma.com/dell-inspiron15'),
(25, 'Amazon', 90490.00, 'https://amazon.in/zenbook14-oled'),
(25, 'Flipkart', 88990.00, 'https://flipkart.com/zenbook14-oled'),
(25, 'Croma', 89490.00, 'https://croma.com/zenbook14-oled'),
(26, 'Amazon', 54999.00, 'https://amazon.in/acer-aspire5'),
(26, 'Flipkart', 53999.00, 'https://flipkart.com/acer-aspire5'),
(26, 'Croma', 55499.00, 'https://croma.com/acer-aspire5'),
(27, 'Amazon', 248999.00, 'https://amazon.in/surface-laptop-studio2'),
(27, 'Flipkart', 250499.00, 'https://flipkart.com/surface-laptop-studio2'),
(27, 'Croma', 249499.00, 'https://croma.com/surface-laptop-studio2'),
(28, 'Amazon', 89999.00, 'https://amazon.in/pavilion-aero13'),
(28, 'Flipkart', 88499.00, 'https://flipkart.com/pavilion-aero13'),
(28, 'Croma', 90499.00, 'https://croma.com/pavilion-aero13'),
(29, 'Amazon', 179490.00, 'https://amazon.in/galaxy-book3ultra'),
(29, 'Flipkart', 178990.00, 'https://flipkart.com/galaxy-book3ultra'),
(29, 'Croma', 180490.00, 'https://croma.com/galaxy-book3ultra'),
(30, 'Amazon', 94999.00, 'https://amazon.in/legion5-gaming'),
(30, 'Flipkart', 93999.00, 'https://flipkart.com/legion5-gaming'),
(30, 'Croma', 95499.00, 'https://croma.com/legion5-gaming');