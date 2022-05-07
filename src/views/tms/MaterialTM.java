package views.tms;

public class MaterialTM {
    private String des;
    private int qty;
    private double price;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
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

    public MaterialTM(String des, int qty, double price) {
        this.des = des;
        this.qty = qty;
        this.price = price;
    }
}
