package views.tms;

public class OrderDetailTM {
    private String orderId;
    private String code;
    private int qty;
    private double price;
    private double discount;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public OrderDetailTM(String orderId, String code, int qty, double price, double discount) {
        this.orderId = orderId;
        this.code = code;
        this.qty = qty;
        this.price = price;
        this.discount = discount;
    }
}
