package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Product;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        this.connection = DBConnection.getConnection();
    }

    public List<Product> searchProducts(String query) {
        List<Product> products = new ArrayList<>();
        String searchQuery = "%" + query.toLowerCase() + "%";
        
        String sql = "SELECT p.*, pp.platform, pp.price, pp.url " +
                    "FROM products p " +
                    "LEFT JOIN product_prices pp ON p.id = pp.product_id " +
                    "WHERE LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ? " +
                    "ORDER BY p.name, pp.price ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, searchQuery);
            stmt.setString(2, searchQuery);
            
            ResultSet rs = stmt.executeQuery();
            Map<Integer, Product> productMap = new HashMap<>();
            
            while (rs.next()) {
                int productId = rs.getInt("id");
                Product product = productMap.get(productId);
                
                if (product == null) {
                    product = new Product(
                        productId,
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("category")
                    );
                    productMap.put(productId, product);
                    products.add(product);
                }
                
                if (rs.getString("platform") != null) {
                    Product.ProductPrice price = new Product.ProductPrice(
                        rs.getString("platform"),
                        rs.getDouble("price"),
                        rs.getString("url")
                    );
                    product.addPrice(price);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }
}
    