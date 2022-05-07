package views.tms;

public class InvoiceTM {
    private String item;
    private double price;
    private int qty;
    private double discount;
    private double amount;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public InvoiceTM(String item, double price, int qty, double discount, double amount) {
        this.item = item;
        this.price = price;
        this.qty = qty;
        this.discount = discount;
        this.amount = amount;
    }
}
