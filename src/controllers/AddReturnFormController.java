package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;
import util.ValidationUtil;
import views.tms.ItemTM;
import views.tms.OrderDetailTM;
import views.tms.ReturnTM;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class AddReturnFormController {
    public JFXComboBox<String> cmbOrderId;
    public JFXComboBox<String> cmbItemCode;
    public JFXTextField txtReason;
    public JFXTextField txtQty;
    public JFXTextField txtAmount;
    public JFXTextField txtDes;
    public JFXButton btnReturn;

    public void initialize() {
        try {
            cmbOrderId.setEditable(true);
            cmbOrderId.getItems().addAll(OrderCrudController.getOrderIds());
            TextFields.bindAutoCompletion(cmbOrderId.getEditor(), cmbOrderId.getItems());
            cmbItemCode.setEditable(true);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbOrderId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setItemCodes(newValue);
        });
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setItemDescriptions(newValue);
        });
        btnReturn.setDisable(true);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Pattern reasonPattern = Pattern.compile("[A-z0-9 ,/]{2,}");
        if (!reasonPattern.matcher(txtReason.getText()).matches()) {
            ValidationUtil.addError(txtReason, btnReturn);
        } else {
            ValidationUtil.removeError(txtReason, btnReturn);
            if (keyEvent.getCode() == KeyCode.ENTER) {
                txtQty.requestFocus();
            }
        }
    }

    private void setItemDescriptions(String code) {
        if (code != null) {
            try {
                ItemTM tm = ItemCrudController.getItem(code);
                txtDes.setText(tm.getDescription());
                txtReason.requestFocus();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void setItemCodes(String code) {
        if (code != null) {
            cmbItemCode.getItems().clear();
            txtDes.clear();
            try {
                ObservableList<String> itemCodes = ReturnCrudController.getItemCodes(code);
                cmbItemCode.getItems().setAll(itemCodes);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnReturnOnAction(ActionEvent actionEvent) {
        returnItem();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    private void returnItem() {
        ReturnTM tm = new ReturnTM(
                cmbOrderId.getValue(),
                cmbItemCode.getValue(),
                txtDes.getText(),
                txtReason.getText(),
                LocalDate.now().toString(),
                Double.parseDouble(txtAmount.getText()),
                Integer.parseInt(txtQty.getText())
        );
        ReturnCrudController.returnItem(tm);
        TrayNotification tray = new TrayNotification("Returned", "Successful", NotificationType.SUCCESS);
        tray.showAndDismiss(Duration.millis(2000));
    }

    public void btnCloseOnAction(MouseEvent mouseEvent) {
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
    }

    public void txtQtyOnAction(KeyEvent keyEvent) {
        Pattern qtyPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");
        try {
            OrderDetailTM tm = ReturnCrudController.getOrderItemDetail(cmbOrderId.getValue(), cmbItemCode.getValue());
            if (qtyPattern.matcher(txtQty.getText()).matches()) {
                if (!txtQty.getText().isEmpty()) {
                    double amount = tm.getPrice() * Double.parseDouble(txtQty.getText());
                    txtAmount.setText(String.valueOf(amount));
                    if (tm.getQty() < Integer.parseInt(txtQty.getText())) {
                        ValidationUtil.addError(txtQty, btnReturn);
                        txtAmount.clear();
                    } else {
                        ValidationUtil.removeError(txtQty, btnReturn);
                        if (keyEvent.getCode() == KeyCode.ENTER) {
                            returnItem();
                            ((Node) (keyEvent.getSource())).getScene().getWindow().hide();
                        }
                    }
                } else {
                    txtAmount.clear();
                }
            } else {
                ValidationUtil.addError(txtQty, btnReturn);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtDes.clear();
        txtAmount.clear();
        txtReason.clear();
        txtQty.clear();
    }
}
