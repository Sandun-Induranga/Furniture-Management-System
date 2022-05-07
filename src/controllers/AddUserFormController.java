package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.User;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;
import util.PrimaryKeyUtil;
import util.ValidationUtil;
import views.tms.ItemTM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class AddUserFormController {
    public static boolean isEditButton;
    public JFXTextField txtName;
    public JFXComboBox<String> cmbRole;
    public JFXTextField txtUserName;
    public JFXTextField txtPassword;
    public JFXTextField txtPasswordAgain;
    public Label lblError;
    public JFXButton btnSave;
    public static String name;
    public static String userName;
    public static String password;
    public static String role;
    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {

        if (isEditButton) {
            btnSave.setText("UPDATE");
            txtUserName.setText(userName);
            cmbRole.getSelectionModel().select(role);
            txtPassword.setText(password);
            txtName.setText(String.valueOf(name));
        }else {
            btnSave.setText("SAVE");
        }
        btnSave.setDisable(true);
        cmbRole.getItems().addAll("Admin", "User");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,}$");
        Pattern usernamePattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern passwordPattern = Pattern.compile("^[A-z0-9 @!#$%^&*()=+?<>,/]{4,20}$");

        map.put(txtName,namePattern);
        map.put(txtUserName,usernamePattern);
        map.put(txtPassword,passwordPattern);
        txtPasswordAgain.setStyle("-fx-text-fill: #CDCDCD");
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map, btnSave);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = ValidationUtil.validate(map, btnSave);

            if (response instanceof JFXTextField) {
                JFXTextField textField = (JFXTextField) response;
                textField.setStyle("-fx-prompt-text-fill: #CDCDCD");
                textField.requestFocus();
            } else if (response instanceof Boolean) {
                saveUser(keyEvent);
            }
        }
    }

    private void saveUser(Event event) {
        if (btnSave.getText().equals("SAVE")) {
            if (txtPassword.getText().equals(txtPasswordAgain.getText())) {
                User user = new User(txtUserName.getText(), txtName.getText(), txtPassword.getText(), cmbRole.getValue());
                try {
                    UserCrudController.addUser(user);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }else {
                lblError.setText("Password is not matched");
            }
        }else {
            if (txtPassword.getText().equals(txtPasswordAgain.getText())) {
                User user = new User(txtUserName.getText(), txtName.getText(), txtPassword.getText(), cmbRole.getValue());
                editUser(user);
            }else {
                lblError.setText("Password is not matched");
            }
            isEditButton = false;
        }

    }

    public void btnCloseOnAction(MouseEvent mouseEvent) {
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        saveUser(actionEvent);
    }

    public void editUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)) {
            try {
                CrudUtil.execute("UPDATE User SET userName=?,name=?,role=?,password=? WHERE userName=?",
                        user.getUsername(),
                        user.getName(),
                        user.getRole(),
                        user.getPassword(),
                        userName
                );
                TrayNotification tray = new TrayNotification("Edited", "Successful", NotificationType.SUCCESS);
                tray.showAndDismiss(Duration.millis(2000));
                isEditButton = false;
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtName.clear();
        txtPassword.clear();
        txtUserName.clear();
        txtPasswordAgain.clear();
        cmbRole.getSelectionModel().clearSelection();
    }
}
