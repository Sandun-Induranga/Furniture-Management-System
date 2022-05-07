package views.tms;

public class CreditTM {
    private String orderId;
    private String cusId;
    private String cusName;
    private double amount;
    private String dueDate;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
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

    public CreditTM(String orderId, String cusId, String cusName, double amount, String dueDate) {
        this.orderId = orderId;
        this.cusId = cusId;
        this.cusName = cusName;
        this.amount = amount;
        this.dueDate = dueDate;
    }
}
