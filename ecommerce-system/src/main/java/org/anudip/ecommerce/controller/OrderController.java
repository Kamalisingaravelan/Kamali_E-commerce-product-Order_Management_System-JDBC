package org.anudip.ecommerce.controller;

import org.anudip.ecommerce.model.Order;
import org.anudip.ecommerce.model.OrderItem;
import org.anudip.ecommerce.service.OrderService;

import java.util.List;

public class OrderController {
    private final OrderService service = new OrderService();

    public int createOrder(Order order, List<OrderItem> items) {
        return service.placeOrder(order, items);
    }
}
