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
public class WishlistDAO {
    private Connection connection;

    public WishlistDAO() {
        connection = DBConnection.getConnection();
    }

    public boolean addToWishlist(int userId, int productId) {
        String sql = "INSERT INTO wishlists (user_id, product_id) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFromWishlist(int userId, int productId) {
        String sql = "DELETE FROM wishlists WHERE user_id = ? AND product_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Product> getWishlist(int userId) {
        List<Product> wishlist = new ArrayList<>();
        String sql = "SELECT p.*, pp.platform, pp.price, pp.url " +
                    "FROM wishlists w " +
                    "JOIN products p ON w.product_id = p.id " +
                    "LEFT JOIN product_prices pp ON p.id = pp.product_id " +
                    "WHERE w.user_id = ? " +
                    "ORDER BY p.category, p.name, pp.price ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
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
                    wishlist.add(product);
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
        
        return wishlist;
    }
    public boolean isInWishlist(int userId, int productId) {
        String sql = "SELECT COUNT(*) FROM wishlists WHERE user_id = ? AND product_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
