package org.anudip.ecommerce.service;

import org.anudip.ecommerce.dao.CartDAO;
import org.anudip.ecommerce.model.CartItem;

import java.util.List;
import java.util.logging.Logger;

public class CartService {
    private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());
    private final CartDAO dao = new CartDAO();

    public int addToCart(int userId, int productId, int qty) {
        if (qty <= 0) {
            System.out.println("❌ Quantity must be at least 1.");
            return -1;
        }
        int id = dao.addToCart(userId, productId, qty);
        if (id > 0) System.out.println("✅ Added to cart (cartId=" + id + ")");
        else System.out.println("❌ Failed to add to cart.");
        return id;
    }

    public List<CartItem> viewCart(int userId) {
        List<CartItem> items = dao.getUserCart(userId);
        if (items == null || items.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return items;
    }

    public boolean removeItem(int cartId) {
        boolean ok = dao.removeCartItem(cartId);
        if (ok) System.out.println("✅ Removed item from cart.");
        else System.out.println("❌ Failed to remove item (id: " + cartId + ").");
        return ok;
    }

    // ✅ New method: remove using userId + productId (for App.java)
    public boolean removeFromCart(int userId, int productId) {
        boolean ok = dao.removeCartItemByUserAndProduct(userId, productId);
        if (ok) System.out.println("✅ Product removed from your cart.");
        else System.out.println("⚠ Product not found in your cart.");
        return ok;
    }

    public void clearCart(int userId) {
        dao.clearCartByUser(userId);
        System.out.println("✅ Cart cleared.");
    }

    // methods used by checkout which need connection-level operations provided by DAO
    public List<CartItem> getUserCartWithConnection(int userId, java.sql.Connection con) throws java.sql.SQLException {
        return dao.getUserCartWithConnection(userId, con);
    }

    public void deleteCartByUserWithConnection(int userId, java.sql.Connection con) throws java.sql.SQLException {
        dao.deleteCartItemsByUserWithConnection(userId, con);
    }
}

