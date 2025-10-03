package org.anudip.ecommerce.dao;

import org.anudip.ecommerce.model.CartItem;
import org.anudip.ecommerce.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CartDAO {
    private static final Logger LOGGER = Logger.getLogger(CartDAO.class.getName());

    public int addToCart(int userId, int productId, int qty) {
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            LOGGER.severe("addToCart error: " + e.getMessage());
        }
        return -1;
    }

    public List<CartItem> getUserCart(int userId) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT cart_id, user_id, product_id, quantity FROM cart_items WHERE user_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem ci = new CartItem(
                            rs.getInt("cart_id"),
                            rs.getInt("user_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            null   // no created_at in DB
                    );
                    items.add(ci);
                }
            }
        } catch (Exception e) {
            LOGGER.severe("getUserCart error: " + e.getMessage());
        }
        return items;
    }

    public boolean removeCartItem(int cartId) {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            LOGGER.severe("removeCartItem error: " + e.getMessage());
        }
        return false;
    }

    public boolean removeCartItemByUserAndProduct(int userId, int productId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            LOGGER.severe("removeCartItemByUserAndProduct error: " + e.getMessage());
        }
        return false;
    }

    public void clearCartByUser(int userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            LOGGER.severe("clearCartByUser error: " + e.getMessage());
        }
    }

    public List<CartItem> getUserCartWithConnection(int userId, Connection con) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT cart_id, user_id, product_id, quantity FROM cart_items WHERE user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem ci = new CartItem(
                            rs.getInt("cart_id"),
                            rs.getInt("user_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            null
                    );
                    items.add(ci);
                }
            }
        }
        return items;
    }

    public boolean deleteCartItemsByUserWithConnection(int userId, Connection con) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            return true;
        }
    }
}
