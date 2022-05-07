package views.tms;

public class SupplyDetailTM {
    private String code;
    private int qty;
    private Double price;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public SupplyDetailTM(String code, int qty, Double price) {
        this.code = code;
        this.qty = qty;
        this.price = price;
    }
}
