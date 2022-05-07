package util;

import controllers.*;

import java.sql.SQLException;

public class PrimaryKeyUtil {
    public static String setPaymentId() throws SQLException, ClassNotFoundException {
        String lastId = new PaymentsCrudController().getPaymentId();
        int digits = Integer.parseInt(lastId.substring(4));
        String id = "PAY-" + ++digits;
        if (id.length() < 6) {
            id = "PAY-00" + digits;
        } else if (id.length() < 7) {
            id = "PAY-0" + digits;
        } else {
            id = "PAY-" + digits;
        }
        return id;
    }

    public static String getCustomerId() throws SQLException, ClassNotFoundException {
        String lastId = new CustomerCrudController().getCustomerId();
        int digits = Integer.parseInt(lastId.substring(4));
        String id = "CUS-" + ++digits;
        if (id.length() < 6) {
            id = "CUS-00" + digits;
        } else if (id.length() < 7) {
            id = "CUS-0" + digits;
        } else {
            id = "CUS-" + digits;
        }
        return id;
    }

    public static String getSupplierId() throws SQLException, ClassNotFoundException {
        String lastId = new SupplierCrudController().getSupplierId();
        int digits = Integer.parseInt(lastId.substring(4));
        String id = "SUP-" + ++digits;
        if (id.length() < 6) {
            id = "SUP-00" + digits;
        } else if (id.length() < 7) {
            id = "SUP-0" + digits;
        } else {
            id = "SUP-" + digits;
        }
        return id;
    }

    public static String getItemCode() throws SQLException, ClassNotFoundException {
        String lastId = new ItemCrudController().getItemCode();
        int digits = Integer.parseInt(lastId.substring(4));
        String id = "ITM-" + ++digits;
        if (id.length() < 6) {
            id = "ITM-00" + digits;
        } else if (id.length() < 7) {
            id = "ITM-0" + digits;
        } else {
            id = "ITM-" + digits;
        }
        return id;
    }

    public static String getCreditId() throws SQLException, ClassNotFoundException {
        String lastId = new PaymentsCrudController().getCreditId();
        int digits = Integer.parseInt(lastId.substring(4));
        String id = "CRD-" + ++digits;
        if (id.length() < 6) {
            id = "CRD-00" + digits;
        } else if (id.length() < 7) {
            id = "CRD-0" + digits;
        } else {
            id = "CRD-" + digits;
        }
        return id;
    }

    public static String setBuyId() throws SQLException, ClassNotFoundException {
        String lastId = new BuysCrudController().getOrderId();
        int digits = Integer.parseInt(lastId.substring(4));
        String id = "OWR-" + ++digits;
        if (id.length() < 6) {
            id = "OWR-00" + digits;
        } else if (id.length() < 7) {
            id = "OWR-0" + digits;
        } else {
            id = "OWR-" + digits;
        }
        return id;
    }

    public static String setCategoryId() throws SQLException, ClassNotFoundException {
        String lastId = new ItemCrudController().getCategoryId();
        int digits = Integer.parseInt(lastId.substring(4));
        String id = "CAT-" + ++digits;
        if (id.length() < 6) {
            id = "CAT-00" + digits;
        } else if (id.length() < 7) {
            id = "CAT-0" + digits;
        } else {
            id = "CAT-" + digits;
        }
        return id;
    }
}
