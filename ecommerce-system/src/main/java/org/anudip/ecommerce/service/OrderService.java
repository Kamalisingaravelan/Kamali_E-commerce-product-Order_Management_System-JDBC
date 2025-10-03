package org.anudip.ecommerce.service;

import org.anudip.ecommerce.dao.OrderDAO;
import org.anudip.ecommerce.model.Order;
import org.anudip.ecommerce.model.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final OrderDAO dao = new OrderDAO();

    public int placeOrder(Order o, List<OrderItem> items) {
        return dao.createOrder(o, items);
    }

    public List<Order> getByUser(int userId) { return dao.getOrdersByUser(userId); }

    public BigDecimal getTotalRevenue() { return dao.calculateTotalRevenue(); }

    /**
     * Returns list of int[]{productId, qtySold}
     */
    public List<int[]> getTopSellingProducts(int limit) {
        return dao.getTopSellingProducts(limit);
    }
}
