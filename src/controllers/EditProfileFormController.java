package controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;
import model.User;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EditProfileFormController {
    public JFXTextField txtName;
    public JFXTextField txtUsrName;
    public JFXPasswordField txtPassword;
    public JFXPasswordField txtConfirmPassword;
    public Label lblError;


    public void initialize() {
        User user = getUser();
        if (user!=null){
            txtName.setText(user.getName());
            txtUsrName.setText(user.getUsername());
            txtPassword.setText(user.getPassword());
        }
    }

    private User getUser() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM User WHERE userName=?", UserLoginFormController.userName);
            if (resultSet.next()) {
                return new User(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if (txtPassword.getText().equals(txtConfirmPassword.getText())){
            try {
                CrudUtil.execute("UPDATE User SET userName=?,name=?,password=? WHERE userName=?",
                        txtUsrName.getText(),
                        txtName.getText(),
                        txtPassword.getText(),
                        UserLoginFormController.userName
                );
                TrayNotification tray = new TrayNotification("Edited", "Successful", NotificationType.SUCCESS);
                tray.showAndDismiss(Duration.millis(2000));
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }else {
            lblError.setText("Passwords Not Matched");
        }
    }
}
