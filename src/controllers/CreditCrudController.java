package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.CrudUtil;
import views.tms.CreditTM;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditCrudController {
    public static ObservableList<CreditTM> loadAllCredits() throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT o.id, c.id, c.name, cr.amountToPay, cr.dueDate FROM Credit cr JOIN " +
                "CustomerOrder o on cr.orderId = o.id JOIN Customer c ON o.cusId = c.id");

        ObservableList<CreditTM> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new CreditTM(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getDouble(4),
                            result.getString(5)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<CreditTM> loadSearchedCredits(String id) throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT o.id, c.id, c.name, cr.amountToPay, cr.dueDate FROM Credit cr JOIN " +
                "CustomerOrder o on cr.orderId = o.id JOIN Customer c ON o.cusId = c.id WHERE o.id LIKE '%" + id + "%'");

        ObservableList<CreditTM> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new CreditTM(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getDouble(4),
                            result.getString(5)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<CreditTM> loadFilteredCredits() throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT o.id, c.id, c.name, cr.amountToPay, cr.dueDate FROM Credit cr JOIN " +
                "CustomerOrder o on cr.orderId = o.id JOIN Customer c ON o.cusId = c.id WHERE cr.dueDate<NOW()");

        ObservableList<CreditTM> obList = FXCollections.observableArrayList();
        while (result.next()) {
            obList.add(
                    new CreditTM(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getDouble(4),
                            result.getString(5)
                    )
            );
        }
        return obList;
    }

    public static int getCreditNotPaidCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(orderId) FROM Credit WHERE dueDate<now()");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }
}
