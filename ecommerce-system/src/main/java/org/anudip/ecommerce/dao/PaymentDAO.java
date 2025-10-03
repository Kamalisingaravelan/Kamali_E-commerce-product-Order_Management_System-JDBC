package org.anudip.ecommerce.dao;

import org.anudip.ecommerce.model.Payment;
import org.anudip.ecommerce.util.DBUtil;

import java.sql.*;
import java.util.logging.Logger;

public class PaymentDAO {
    private static final Logger LOGGER = Logger.getLogger(PaymentDAO.class.getName());

    public int addPayment(Payment payment) {
        String sql = "INSERT INTO payments(order_id, amount, status) VALUES (?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getOrderId());
            ps.setBigDecimal(2, payment.getAmount());
            ps.setString(3, payment.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    payment.setPaymentId(id);
                    return id;
                }
            }
        } catch (Exception e) {
            LOGGER.severe("addPayment error: " + e.getMessage());
        }
        return -1;
    }
}
