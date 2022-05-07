package controllers;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;
import model.User;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserCrudController {
    public static ObservableList<User> loadAllUsers() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM `User`");

        ObservableList<User> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new User(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<User> loadSearchedUsers(String id) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM `User` WHERE userName LIKE '%" + id + "%'");

        ObservableList<User> obList = FXCollections.observableArrayList();

        while (result.next()) {
            obList.add(
                    new User(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4)
                    )
            );
        }
        return obList;
    }

    public static ObservableList<User> deleteUser(javafx.scene.control.TableView tableView) throws SQLException, ClassNotFoundException {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {
                Object object = tableView.getSelectionModel().getSelectedItem();
                User user = (User) object;
                try {
                    CrudUtil.execute("DELETE FROM User WHERE username = ? ", user.getUsername());
                    TrayNotification tray = new TrayNotification("Deleted", "Successful", NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.millis(2000));
                    return loadAllUsers();
                } catch (SQLException | ClassNotFoundException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            }
        }
        return loadAllUsers();
    }

    public static void addUser(User user) throws SQLException, ClassNotFoundException {
        CrudUtil.execute("INSERT INTO User VALUES (?,?,?,?)",
                user.getUsername(),
                user.getName(),
                user.getPassword(),
                user.getRole()
        );
    }

    public static String getUsersName(String username) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT name FROM User WHERE userName=?", username);
        if (resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    public static User getUser(String id) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM `User` WHERE userName=?", id);

        if (result.next()) {
            return new User(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4)
            );
        }
        return null;
    }
}
