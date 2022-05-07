package views.tms;

public class BuyTM {
    private String orderId;
    private String supId;
    private String supName;
    private String orderDate;
    private double cost;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSupId() {
        return supId;
    }

    public void setSupId(String supId) {
        this.supId = supId;
    }

    public String getSupName() {
        return supName;
    }

    public void setSupName(String supName) {
        this.supName = supName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public BuyTM(String orderId, String supId, String supName, String orderDate, double cost) {
        this.orderId = orderId;
        this.supId = supId;
        this.supName = supName;
        this.orderDate = orderDate;
        this.cost = cost;
    }
}
