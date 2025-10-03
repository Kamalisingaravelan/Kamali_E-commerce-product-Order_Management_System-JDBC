package org.anudip.ecommerce.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private int orderId;
    private BigDecimal amount;
    private String status;
    private Timestamp paymentDate;

    public Payment() {}

    public Payment(int paymentId, int orderId, BigDecimal amount, String status, Timestamp paymentDate) {
        this.paymentId = paymentId; this.orderId = orderId; this.amount = amount; this.status = status; this.paymentDate = paymentDate;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Timestamp paymentDate) { this.paymentDate = paymentDate; }

    @Override
    public String toString() {
        return "Payment{" + "paymentId=" + paymentId + ", orderId=" + orderId + ", amount=" + amount +
                ", status='" + status + '\'' + ", paymentDate=" + paymentDate + '}';
    }
}
