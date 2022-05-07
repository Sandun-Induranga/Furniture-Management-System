package model;

public class Credit {
    private String orderId;
    private double amount;
    private String dueDate;

    public Credit(String orderId, double amount, String dueDate) {
        this.orderId = orderId;
        this.amount = amount;
        this.dueDate = dueDate;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
