package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;
import util.PrimaryKeyUtil;
import util.ValidationUtil;
import views.tms.SupplierTM;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class AddSupplierFormController {
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtEmail;
    public JFXTextField txtPhoneNumber;
    public JFXTextField txtDes;
    public static String id;
    public static String name;
    public static String address;
    public static String email;
    public static String phoneNumber;
    public static boolean isEditButton;
    public static String des;
    public JFXButton btnSave;
    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {
        if (!isEditButton) {
            try {
                txtId.setText(PrimaryKeyUtil.getSupplierId());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            txtName.requestFocus();
        } else {
            btnSave.setText("UPDATE");
            txtId.setText(id);
            txtName.setText(name);
            txtDes.setText(des);
            txtAddress.setText(address);
            txtEmail.setText(email);
            txtPhoneNumber.setText(phoneNumber);
        }
        Pattern namePattern = Pattern.compile("^[A-z ]{3,40}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,40}$");
        Pattern emailPattern = Pattern.compile("[A-z0-9]{3,}@[A-z]{3,7}.com$");
        Pattern desPattern = Pattern.compile("[A-z0-9 ,/]{2,}");
        Pattern phoneNumberPattern = Pattern.compile("^(070|072|074|075|076|077|078)[0-9]{7}$");
        btnSave.setDisable(true);
        map.put(txtName, namePattern);
        map.put(txtDes, desPattern);
        map.put(txtAddress, addressPattern);
        map.put(txtEmail, emailPattern);
        map.put(txtPhoneNumber, phoneNumberPattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map, btnSave);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = ValidationUtil.validate(map, btnSave);

            if (response instanceof JFXTextField) {
                JFXTextField textField = (JFXTextField) response;
                textField.setStyle("-fx-font-size: 20; -fx-prompt-text-fill: #A7A7A7");
                textField.requestFocus();
            } else if (response instanceof Boolean) {
                System.out.println("Work");
                saveSupplier();
                ((Node) (keyEvent.getSource())).getScene().getWindow().hide();
            }
        }
    }

    public void closeOnAction(MouseEvent mouseEvent) {
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        saveSupplier();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    private void saveSupplier() {
        if (btnSave.getText().equals("SAVE")) {
            SupplierTM tm = new SupplierTM(txtId.getText(), txtName.getText(), txtDes.getText(), txtAddress.getText(), txtEmail.getText(), txtPhoneNumber.getText());
            SupplierCrudController.addSupplier(tm);
            TrayNotification tray = new TrayNotification("Saved", "Successful", NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(2000));
        } else {
            SupplierTM tm = new SupplierTM(
                    txtId.getText(),
                    txtName.getText(),
                    txtDes.getText(),
                    txtAddress.getText(),
                    txtEmail.getText(),
                    txtPhoneNumber.getText()
            );
            editSupplier(tm);
        }
    }

    public void editSupplier(SupplierTM tm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)) {
            try {
                CrudUtil.execute("UPDATE Supplier SET id=?,name=?,description=?,address=?,email=?,phoneNumber=? WHERE id=?",
                        tm.getId(),
                        tm.getName(),
                        tm.getDescription(),
                        tm.getAddress(),
                        tm.getEmail(),
                        tm.getPhoneNumber(),
                        txtId.getText()
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
        txtDes.clear();
        txtEmail.clear();
        txtAddress.clear();
        txtPhoneNumber.clear();
    }
}
