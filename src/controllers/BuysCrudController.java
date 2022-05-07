package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import model.Customer;
import util.CrudUtil;
import views.tms.MaterialTM;
import views.tms.BuyTM;
import views.tms.SupplyDetailTM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class BuysCrudController {
    public static ObservableList<BuyTM> loadAllBuys() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT oo.id,s.id,s.name,oo.date,oo.cost FROM OwnerOrder oo JOIN Supplier s ON oo.supId=s.id");

        ObservableList<BuyTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new BuyTM(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getDouble(5)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<BuyTM> loadSearchedBuys(String id) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT oo.id,s.id,s.name,oo.date,oo.cost FROM OwnerOrder oo JOIN Supplier s ON oo.supId=s.id WHERE oo.id LIKE '%"+id+"%'");

        ObservableList<BuyTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new BuyTM(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getDouble(5)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<BuyTM> loadFilteredBuys(String begin, String end) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT oo.id,s.id,s.name,oo.date,oo.cost FROM OwnerOrder oo JOIN Supplier s ON oo.supId=s.id WHERE oo.date BETWEEN ? AND ?",begin,end);

        ObservableList<BuyTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new BuyTM(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getDouble(5)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<BuyTM> deleteBuys(TableView tableView) throws SQLException, ClassNotFoundException {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                Object object = tableView.getSelectionModel().getSelectedItem();
                BuyTM buyTM = (BuyTM) object;
                try {
                    CrudUtil.execute("DELETE FROM OwnerOrder WHERE id = ? ", buyTM.getOrderId());
                    new Alert(Alert.AlertType.INFORMATION, "SUCCESS").show();
                    return loadAllBuys();
                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            }
        }
        return loadAllBuys();
    }

    public static ObservableList<SupplyDetailTM> loadBuyDetails(BuyTM tm) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM SupplyDetail WHERE orderId=?",tm.getOrderId());

        ObservableList<SupplyDetailTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new SupplyDetailTM(
                            result.getString(2),
                            result.getInt(3),
                            result.getDouble(4)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<MaterialTM> loadMaterialDetails(BuyTM tm) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM MaterialDetail WHERE orderId=?",tm.getOrderId());

        ObservableList<MaterialTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new MaterialTM(
                            result.getString(2),
                            result.getInt(3),
                            result.getDouble(4)
                    )
            );
        }
        return obList;
    }

    public static void addBuys(BuyTM tm) {
        try {
            CrudUtil.execute("INSERT INTO OwnerOrder VALUES (?,?,?,?,?)", tm.getOrderId(),
                    tm.getSupId(),
                    tm.getCost(),
                    tm.getOrderDate(),
                    LocalTime.now().toString()
            );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM OwnerOrder ORDER BY id DESC LIMIT 1");
        if (set.next()) {
            return set.getString(1);
        } else {
            return "OWR-001";
        }
    }

    public static int getMonthBuys() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(id) FROM OwnerOrder WHERE MONTH(date) = MONTH(CURRENT_DATE())AND YEAR(date) = YEAR(CURRENT_DATE())");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static int getMonthTotalBuysAmount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(cost) FROM OwnerOrder WHERE MONTH(date) = MONTH(CURRENT_DATE())AND YEAR(date) = YEAR(CURRENT_DATE())");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static int getYearBuys() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(id) FROM OwnerOrder WHERE YEAR(date) = YEAR(CURRENT_DATE())");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static int getYearTotalBuysAmount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT SUM(cost) FROM OwnerOrder WHERE YEAR(date) = YEAR(CURRENT_DATE())");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }
}
