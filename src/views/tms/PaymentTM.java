package views.tms;

public class PaymentTM {
    private String paymentId;
    private String orderId;
    private double amount;
    private String date;
    private String time;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public PaymentTM(String paymentId, String orderId, double amount, String date, String time) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.date = date;
        this.time = time;
    }
}
