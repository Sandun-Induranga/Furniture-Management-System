package views.tms;

public class SaleTM {
    private String orderId;
    private String cusId;
    private String date;
    private String time;
    private double cost;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SaleTM(String orderId, String cusId, String date, String time, double cost, String status) {
        this.orderId = orderId;
        this.cusId = cusId;
        this.date = date;
        this.time = time;
        this.cost = cost;
        this.status = status;
    }
}
