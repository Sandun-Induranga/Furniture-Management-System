package views.tms;


public class OwnerCartTm {
    private String code;
    private String description;
    private double qty;
    private double cost;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public OwnerCartTm(String code, String description, double qty, double cost) {
        this.code = code;
        this.description = description;
        this.qty = qty;
        this.cost = cost;
    }
}
