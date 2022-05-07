package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.CrudUtil;
import views.tms.CustomerOrderTM;
import views.tms.OrderDetailTM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderCrudController {
    public boolean saveOrder(CustomerOrderTM order) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO CustomerOrder VALUES (?,?,?,?,?,?)",
                order.getOrderId(), order.getCusId(), order.getCost(), order.getDate(), order.getTime(), order.getStatus());
    }

    public boolean saveOrderDetails(ArrayList<OrderDetailTM> details) throws SQLException, ClassNotFoundException {
        for (OrderDetailTM det :
                details) {
            boolean isDetailsSaved = CrudUtil.execute("INSERT INTO SellingDetail VALUES (?,?,?,?,?)",
                    det.getOrderId(), det.getCode(), det.getQty(), det.getPrice(), det.getDiscount());
            if (isDetailsSaved) {
                if (!updateQty(det.getCode(), det.getQty())) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Item SET qtyOnHand=qtyOnHand-? WHERE code=?", qty, itemCode);
    }

    public static ObservableList<String> getOrderIds() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM CustomerOrder");
        ObservableList<String> observableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            observableList.add(resultSet.getString(1));
        }
        return observableList;
    }

    public static double getPaidAmount(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(amount) FROM Payment GROUP BY orderId HAVING orderId=?",id);
        if (resultSet.next()) {
            return resultSet.getDouble(1);
        }
        return 0;
    }

    public static double getAmount(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM CustomerOrder WHERE id=?",id);
        if (resultSet.next()) {
            return resultSet.getDouble(3);
        }
        return -1;
    }
}
