package org.anudip.ecommerce.service;

import org.anudip.ecommerce.dao.PaymentDAO;
import org.anudip.ecommerce.model.Payment;

public class PaymentService {
    private final PaymentDAO dao = new PaymentDAO();

    public int addPayment(Payment p) { return dao.addPayment(p); }
}
