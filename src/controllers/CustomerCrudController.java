package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import model.Customer;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CustomerCrudController {
    public static ObservableList<Customer> loadAllCustomers() throws ClassNotFoundException, SQLException {
        String sql = "SELECT * FROM Customer";
        ResultSet result = CrudUtil.execute(sql);

        ObservableList<Customer> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new Customer(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<Customer> deleteCustomer(TableView tableView) throws SQLException, ClassNotFoundException {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                Object object = tableView.getSelectionModel().getSelectedItem();
                Customer customer = (Customer) object;
                try {
                    CrudUtil.execute("DELETE FROM Customer WHERE id = ? ", customer.getId());
                    TrayNotification tray = new TrayNotification("Deleted", "Successful", NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.millis(2000));
                    return loadAllCustomers();
                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            }
        }
        return loadAllCustomers();
    }

    public static void addCustomer(Customer c) {
        try {
            CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?,?)", c.getId(),
                    c.getName(),
                    c.getAddress(),
                    c.getPhoneNumber()
            );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> loadAllCustomerIds() throws SQLException, ClassNotFoundException {
        String sql = "SELECT id FROM Customer";
        ResultSet result = CrudUtil.execute(sql);

        ObservableList<String> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(result.getString(1));
        }
        return obList;
    }

    public static Customer getCustomer(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Customer WHERE id = ?";
        ResultSet result = CrudUtil.execute(sql, id);

        if (result.next()) {
            return
                    new Customer(result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4)
                    );
        }
        return null;
    }

    public String getCustomerId() throws SQLException, ClassNotFoundException {
        ResultSet set = CrudUtil.execute("SELECT id FROM Customer ORDER BY id DESC LIMIT 1");
        if (set.next()) {
            return set.getString(1);
        } else {
            return "CUS-001";
        }
    }

    public static ObservableList<Customer> searchCustomers(String id) throws ClassNotFoundException, SQLException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer WHERE id LIKE '%"+id+"%'");

        ObservableList<Customer> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new Customer(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4)
                    )
            );
        }
        return obList;
    }

}
