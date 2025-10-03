package org.anudip.ecommerce.dao;

import org.anudip.ecommerce.model.Order;
import org.anudip.ecommerce.model.OrderItem;
import org.anudip.ecommerce.util.DBUtil;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Creates order + order_items and reduces stock in a single transaction.
     * Returns generated order_id or -1 on failure.
     */
    public int createOrder(Order order, List<OrderItem> items) {
        String insertOrderSql = "INSERT INTO orders(user_id, total_amount) VALUES (?, ?)";
        String insertItemSql = "INSERT INTO order_items(order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        Connection con = null;
        try {
            con = DBUtil.getConnection();
            con.setAutoCommit(false);

            // insert order
            try (PreparedStatement ps = con.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getUserId());
                ps.setBigDecimal(2, order.getTotalAmount() == null ? BigDecimal.ZERO : order.getTotalAmount());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("Failed to create order");
                    int orderId = rs.getInt(1);

                    // insert items: reduce stock then insert item
                    try (PreparedStatement ips = con.prepareStatement(insertItemSql)) {
                        for (OrderItem oi : items) {
                            boolean reduced = productDAO.reduceStockForUpdate(oi.getProductId(), oi.getQuantity(), con);
                            if (!reduced) {
                                throw new SQLException("Insufficient stock for product_id=" + oi.getProductId());
                            }
                            ips.setInt(1, orderId);
                            ips.setInt(2, oi.getProductId());
                            ips.setInt(3, oi.getQuantity());
                            ips.setBigDecimal(4, oi.getPrice());
                            ips.addBatch();
                        }
                        ips.executeBatch();
                    }

                    con.commit();
                    return orderId;
                }
            }
        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ignored) {}
            e.printStackTrace();
            return -1;
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (Exception ignored) {}
        }
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT order_id, user_id, order_date, total_amount FROM orders WHERE user_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order();
                    o.setOrderId(rs.getInt("order_id"));
                    o.setUserId(rs.getInt("user_id"));
                    o.setOrderDate(rs.getTimestamp("order_date"));
                    o.setTotalAmount(rs.getBigDecimal("total_amount"));
                    list.add(o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public BigDecimal calculateTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount),0) AS revenue FROM orders";
        try (Connection con = DBUtil.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBigDecimal("revenue");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    /**
     * Top selling products (product_id, total_qty) limit N
     */
    public List<int[]> getTopSellingProducts(int limit) {
        List<int[]> result = new ArrayList<>();
        String sql = "SELECT product_id, SUM(quantity) AS sold_qty FROM order_items GROUP BY product_id ORDER BY sold_qty DESC LIMIT ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new int[]{ rs.getInt("product_id"), rs.getInt("sold_qty") });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

