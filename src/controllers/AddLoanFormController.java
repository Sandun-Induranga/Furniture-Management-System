package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Credit;

import java.sql.SQLException;

public class AddLoanFormController {
    public JFXTextField txtCreditId;
    public JFXTextField txtOrderId;
    public JFXTextField txtAmount;
    public JFXDatePicker txtDueDate;
    public static String amount;
    public static String orderId;
    public JFXButton btnSave;

    public void initialize() {
        txtOrderId.setText(orderId);
        txtAmount.setText(amount);
        btnSave.setDisable(true);
    }

    public void date_Key_Released(KeyEvent keyEvent) {
        if (!txtDueDate.getValue().toString().isEmpty()) {
            btnSave.setDisable(false);
            if (keyEvent.getCode() == KeyCode.ENTER) {
                try {
                    if (OrderCrudController.getOrderIds().contains(txtDueDate.getValue())) {
                        saveCredit();
                        ((Node) (keyEvent.getSource())).getScene().getWindow().hide();
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            btnSave.setDisable(true);
        }

    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        saveCredit();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    private void saveCredit() {
        Credit tm = new Credit(txtOrderId.getText(), Double.parseDouble(txtAmount.getText()), txtDueDate.getValue().toString());
        PaymentsCrudController.addCredit(tm);
    }

    public void btnCloseOnAction(MouseEvent mouseEvent) {
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
    }
}
