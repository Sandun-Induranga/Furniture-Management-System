package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.CrudUtil;
import views.tms.OrderDetailTM;
import views.tms.ReturnTM;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReturnCrudController {
    public static ObservableList<ReturnTM> loadAllReturns() throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT r.orderId,r.code,i.description,r.description,r.date,r.qty,r.amount FROM `Return` r JOIN Item i ON r.code=i.code");


        return getReturnTMS(result);
    }

    private static ObservableList<ReturnTM> getReturnTMS(ResultSet result) throws SQLException {
        ObservableList<ReturnTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(new ReturnTM(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getString(5),
                            result.getDouble(6),
                            result.getInt(7)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<ReturnTM> loadSearchedReturns(String id) throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT r.orderId,r.code,i.description,r.description,r.date,r.qty,r.amount FROM `Return` r JOIN Item i ON r.code=i.code WHERE orderId LIKE '%"+id+"%'");


        return getReturnTMS(result);
    }

    public static ObservableList<ReturnTM> loadFilteredReturns(String begin, String end) throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT r.orderId,r.code,i.description,r.description,r.date,r.qty,r.amount FROM `Return` r JOIN Item i ON r.code=i.code WHERE r.date BETWEEN ? AND ?",begin,end);


        return getReturnTMS(result);
    }

    public static ObservableList<String> getItemCodes(String orderId) throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT * FROM SellingDetail WHERE orderId = ?", orderId);
        ObservableList<String> observableList = FXCollections.observableArrayList();
        while (set.next()) {
            observableList.add(set.getString(2));
        }
        return observableList;
    }

    public static OrderDetailTM getOrderItemDetail(String orderId, String code) throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT * FROM SellingDetail WHERE orderId = ? && code=?", orderId, code);
        if (set.next()) {
            return new OrderDetailTM(
                    set.getString(1),
                    set.getString(2),
                    set.getInt(3),
                    set.getDouble(4),
                    set.getDouble(5)
            );
        }
        return null;
    }

    public static void returnItem(ReturnTM tm) {
        try {
            CrudUtil.execute("INSERT INTO `Return` VALUES (?,?,?,?,?,?)",
                    tm.getId(),
                    tm.getCode(),
                    tm.getReason(),
                    tm.getDate(),
                    tm.getQty(),
                    tm.getAmount()
            );
            CrudUtil.execute("UPDATE Item SET qtyOnHand=qtyOnHand+? WHERE code=?", tm.getQty(),tm.getCode());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
