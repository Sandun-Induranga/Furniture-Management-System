package views.tms;

public class ItemDetailTM {
    private String code;
    private int qty;
    private double price;
    private double discount;

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

    public ItemDetailTM(String code, int qty, double price, double discount) {
        this.code = code;
        this.qty = qty;
        this.price = price;
        this.discount = discount;
    }
}
