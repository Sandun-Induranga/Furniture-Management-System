package controllers;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Credit;
import util.CrudUtil;
import views.tms.CreditTM;
import views.tms.PaymentTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentsCrudController {
    public static ObservableList<PaymentTM> loadAllPayments() throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Payment");

        ObservableList<PaymentTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(new PaymentTM(
                            result.getString(1),
                            result.getString(2),
                            result.getDouble(3),
                            result.getString(4),
                            result.getString(5)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<PaymentTM> loadSearchedPayments(String id) throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Payment WHERE orderId LIKE '%" + id + "%'");

        ObservableList<PaymentTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(new PaymentTM(
                            result.getString(1),
                            result.getString(2),
                            result.getDouble(3),
                            result.getString(4),
                            result.getString(5)
                    )
            );
        }
        return obList;
    }

    public static void addPayment(PaymentTM tm) {
        try {
            CrudUtil.execute("INSERT INTO Payment VALUES (?,?,?,?,?)", tm.getPaymentId(), tm.getOrderId(), tm.getAmount(), tm.getDate(), tm.getTime());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getPaymentId() throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM Payment ORDER BY id DESC LIMIT 1");
        if (set.next()) {
            return set.getString(1);
        } else {
            return "PAY-001";
        }
    }

    public static void addCredit(Credit tm) {
        try {
            CrudUtil.execute("INSERT INTO Credit VALUES (?,?,?)", tm.getOrderId(), tm.getAmount(), tm.getDueDate());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getCreditId() throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM Credit ORDER BY id DESC LIMIT 1");
        if (set.next()) {
            return set.getString(1);
        } else {
            return "CRD-001";
        }
    }
}
