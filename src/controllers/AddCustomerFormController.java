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
import model.Customer;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;
import util.PrimaryKeyUtil;
import util.ValidationUtil;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class AddCustomerFormController {
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtPhoneNumber;
    public static String id;
    public static String name;
    public static String address;
    public static String phoneNumber;
    public static boolean isEditButton;
    public JFXButton btnSave;
    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {
        txtName.requestFocus();
        if (!isEditButton) {
            try {
                txtId.setText(PrimaryKeyUtil.getCustomerId());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            btnSave.setText("UPDATE");
            txtId.setText(id);
            txtName.setText(name);
            txtAddress.setText(address);
            txtPhoneNumber.setText(phoneNumber);
        }

        Pattern namePattern = Pattern.compile("^[A-z ]{3,40}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,40}$");
        Pattern phoneNumberPattern = Pattern.compile("^(070|072|074|075|076|077|078)[0-9]{7}$");

        map.put(txtName, namePattern);
        map.put(txtAddress, addressPattern);
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
                saveCustomer();
                ((Node) (keyEvent.getSource())).getScene().getWindow().hide();
            }
        }
    }

    public void btnCloseOnAction(MouseEvent mouseEvent) {
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
        isEditButton = false;
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        saveCustomer();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    private void saveCustomer() {
        if (btnSave.getText().equals("SAVE")) {
            Customer customer = new Customer(txtId.getText(), txtName.getText(), txtAddress.getText(), txtPhoneNumber.getText());
            CustomerCrudController.addCustomer(customer);
            TrayNotification tray = new TrayNotification("Saved", "Successful", NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(2000));
        } else {
            Customer c = new Customer(
                    txtId.getText(),
                    txtName.getText(),
                    txtAddress.getText(),
                    txtPhoneNumber.getText()
            );
            editCustomer(c);
        }
    }

    public void editCustomer(Customer customer) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)) {
            try {
                CrudUtil.execute("UPDATE Customer SET id=?,name=?,address=?,phoneNumber=? WHERE id=?",
                        customer.getId(),
                        customer.getName(),
                        customer.getAddress(),
                        customer.getPhoneNumber(),
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
        txtAddress.clear();
        txtAddress.clear();
        txtPhoneNumber.clear();
    }
}
