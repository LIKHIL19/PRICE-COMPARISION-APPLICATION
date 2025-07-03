package model;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private String description;
    private String category;
    private List<ProductPrice> prices;

    public Product(int id, String name, String description, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.prices = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public List<ProductPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<ProductPrice> prices) {
        this.prices = prices;
    }

    public void addPrice(ProductPrice price) {
        prices.add(price);
    }

    public double getLowestPrice() {
        if (prices.isEmpty()) {
            return 0.0;
        }
        return prices.stream()
                .mapToDouble(ProductPrice::getPrice)
                .min()
                .getAsDouble();
    }

    public String getBestPlatform() {
        if (prices.isEmpty()) {
            return "-";
        }
        return prices.stream()
                .min((a, b) -> Double.compare(a.getPrice(), b.getPrice()))
                .map(ProductPrice::getPlatform)
                .orElse("-");
    }
    public static class ProductPrice {
        private String platform;
        private double price;
        private String url;
        public ProductPrice(String platform, double price, String url) {
            this.platform = platform;
            this.price = price;
            this.url = url;
        }
        public String getPlatform() {
            return platform;
        }
        public double getPrice() {
            return price;
        }
        public String getUrl() {
            return url;
        }
    }
}