package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import util.CrudUtil;
import views.tms.ItemDetailTM;
import views.tms.SaleTM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class SalesCrudController {
    public static ObservableList<SaleTM> loadAllSales() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT c.id,co.id,co.orderDate,co.orderTime,co.cost,co.paymentStatus FROM CustomerOrder co JOIN Customer c ON co.cusId=c.id");

        return getSaleTMS(result);
    }

    public static ObservableList<SaleTM> loadFilteredSales(String begin, String end) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT c.id,co.id,co.orderDate,co.orderTime,co.cost,co.paymentStatus FROM CustomerOrder co JOIN Customer c ON co.cusId=c.id WHERE co.orderDate BETWEEN ? AND ?", begin, end);

        return getSaleTMS(result);
    }

    public static ObservableList<SaleTM> loadSearchedSales(String text) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT c.id,co.id,co.orderDate,co.orderTime,co.cost,co.paymentStatus FROM CustomerOrder co JOIN Customer c ON co.cusId=c.id WHERE co.id LIKE '%" + text + "%'");

        return getSaleTMS(result);
    }

    private static ObservableList<SaleTM> getSaleTMS(ResultSet result) throws SQLException {
        ObservableList<SaleTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new SaleTM(
                            result.getString(2),
                            result.getString(1),
                            result.getString(3),
                            result.getString(4),
                            result.getDouble(5),
                            result.getString(6)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<SaleTM> deleteOrder(TableView tableView) throws SQLException, ClassNotFoundException {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                Object object = tableView.getSelectionModel().getSelectedItem();
                SaleTM saleTM = (SaleTM) object;
                try {
                    CrudUtil.execute("DELETE FROM CustomerOrder WHERE id = ? ", saleTM.getOrderId());
                    new Alert(Alert.AlertType.INFORMATION, "SUCCESS").show();
                    return loadAllSales();
                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            }
        }
        return loadAllSales();
    }

    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM CustomerOrder ORDER BY id DESC LIMIT 1");
        if (set.next()) {
            return set.getString(1);
        } else {
            return "ORD-001";
        }
    }

    public static ObservableList<ItemDetailTM> loadItemDetails(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM SellingDetail WHERE orderId=?", id);
        ObservableList<ItemDetailTM> observableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            observableList.add(new ItemDetailTM(
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4),
                    resultSet.getDouble(5)
            ));
        }
        return observableList;
    }

    public static int getMonthSales() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(id) FROM CustomerOrder WHERE MONTH(orderDate) = MONTH(CURRENT_DATE())AND YEAR(orderDate) = YEAR(CURRENT_DATE())");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static int getMonthTotalSaleAmount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(cost) FROM CustomerOrder WHERE MONTH(orderDate) = MONTH(CURRENT_DATE())AND YEAR(orderDate) = YEAR(CURRENT_DATE())");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static int getYearSales() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(id) FROM CustomerOrder WHERE YEAR(orderDate) = YEAR(CURRENT_DATE())");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static int getYearTotalSaleAmount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(cost) FROM CustomerOrder WHERE YEAR(orderDate) = YEAR(CURRENT_DATE())");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static ObservableList<Double> getIncomeMonthly() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(cost) FROM CustomerOrder GROUP BY MONTH(orderDate) ORDER BY MONTH(orderDate)");
        ObservableList<Double> observableList = FXCollections.observableArrayList();
        while (resultSet.next()) {
            observableList.add(resultSet.getDouble(1));
        }
        while (observableList.size() < 12) {
            observableList.add(0.0);
        }
        return observableList;
    }


    public static ArrayList<String> getTopProducts() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT code,COUNT(code) FROM SellingDetail GROUP BY code ORDER BY COUNT(code) DESC LIMIT 3");
        ArrayList<String> arrayList = new ArrayList();
        while (resultSet.next()) {
            arrayList.add(resultSet.getString(1));
        }
        return arrayList;
    }

}
