package views.tms;

public class CustomerOrderTM {
    private String orderId;
    private String cusId;
    private double cost;
    private String date;
    private String time;
    private String status;

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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CustomerOrderTM(String orderId, String cusId, double cost, String date, String time, String status) {
        this.orderId = orderId;
        this.cusId = cusId;
        this.cost = cost;
        this.date = date;
        this.time = time;
        this.status = status;
    }
}
