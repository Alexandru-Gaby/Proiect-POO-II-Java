package Model;

import java.time.LocalDate;

public class Payment
{
    private int paymentId;
    private double amount;
    private String paymentMethod;
    private LocalDate paymentDate;

    public Payment(int paymentId, double amount, String paymentMethod, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() { return paymentId; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDate getPaymentDate() { return paymentDate; }

    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
}
