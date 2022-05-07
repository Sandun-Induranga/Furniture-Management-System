package views.tms;

public class ReturnTM {
    private String id;
    private String code;
    private String description;
    private String reason;
    private String date;
    private double amount;
    private int qty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public ReturnTM(String id, String code, String description, String reason, String date, double amount, int qty) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.reason = reason;
        this.date = date;
        this.amount = amount;
        this.qty = qty;
    }
}
