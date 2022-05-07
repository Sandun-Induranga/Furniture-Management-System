package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;
import util.PrimaryKeyUtil;
import util.ValidationUtil;
import views.tms.PaymentTM;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.regex.Pattern;

public class AddPaymentController {
    public JFXTextField txtPaymentId;
    public JFXComboBox<String> cmbOrderId;
    public JFXTextField txtAmount;
    public JFXButton btnSave;

    public void initialize() {
        try {
            txtPaymentId.setText(PrimaryKeyUtil.setPaymentId());
            cmbOrderId.setEditable(true);
            cmbOrderId.getItems().addAll(OrderCrudController.getOrderIds());
            cmbOrderId.requestFocus();
            TextFields.bindAutoCompletion(cmbOrderId.getEditor(), cmbOrderId.getItems());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        btnSave.setDisable(true);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Pattern pricePattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");
        if (!pricePattern.matcher(txtAmount.getText()).matches()) {
            ValidationUtil.addError(txtAmount, btnSave);
        } else {
            ValidationUtil.removeError(txtAmount, btnSave);
            if (keyEvent.getCode() == KeyCode.ENTER) {
                savePayment(keyEvent);
            }
        }
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        savePayment(actionEvent);
    }

    private void savePayment(Event event) {
        PaymentTM tm = new PaymentTM(
                txtPaymentId.getText(),
                cmbOrderId.getValue(),
                Double.parseDouble(txtAmount.getText()),
                LocalDate.now().toString(),
                LocalTime.now().toString()
        );
        try {
            double paidAmount = OrderCrudController.getPaidAmount(tm.getOrderId());
            double totalCost = OrderCrudController.getAmount(tm.getOrderId());
            if (paidAmount + tm.getAmount() < totalCost) {
                PaymentsCrudController.addPayment(tm);
                CrudUtil.execute("UPDATE Credit SET amountToPay=? WHERE orderId=?",
                        totalCost - paidAmount - Double.parseDouble(txtAmount.getText()), tm.getOrderId());
                ((Node) (event.getSource())).getScene().getWindow().hide();
                TrayNotification tray = new TrayNotification("Paid", "Successful", NotificationType.SUCCESS);
                tray.showAndDismiss(Duration.millis(2000));
                generateInvoice();
            } else if (paidAmount + tm.getAmount() == totalCost) {
                PaymentsCrudController.addPayment(tm);
                CrudUtil.execute("UPDATE CustomerOrder SET paymentStatus='PAID' WHERE id=?", tm.getOrderId());
                CrudUtil.execute("DELETE FROM Credit WHERE orderId=?", tm.getOrderId());
                CrudUtil.execute("UPDATE Credit SET dueDate=null WHERE orderId=?", tm.getOrderId());
                CrudUtil.execute("UPDATE Credit SET amountToPay=? WHERE orderId=?",
                        totalCost - paidAmount - Double.parseDouble(txtAmount.getText()), tm.getOrderId());
                TrayNotification tray = new TrayNotification("Paid", "Successful", NotificationType.SUCCESS);
                tray.showAndDismiss(Duration.millis(2000));
                ((Node) (event.getSource())).getScene().getWindow().hide();
                generateInvoice();
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid Payment").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void generateInvoice() {
        try {
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/Payment.jasper"));
            HashMap map = new HashMap();
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM CustomerOrder WHERE id=?", cmbOrderId.getValue());
            String name = null;
            String address = null;
            String phoneNumber = null;
            if (resultSet.next()) {
                ResultSet resultSet1 = CrudUtil.execute("SELECT * FROM Customer WHERE id =?", resultSet.getString(2));
                if (resultSet1.next()) {
                    name = resultSet1.getString(2);
                    address = resultSet1.getString(3);
                    phoneNumber = resultSet1.getString(4);
                }
            }
            map.put("name", name);
            map.put("address", address);
            map.put("phoneNumber", phoneNumber);
            map.put("orderId", cmbOrderId.getValue());
            ResultSet set = CrudUtil.execute("SELECT * FROM Credit WHERE orderId=?", cmbOrderId.getValue());
            String pay = null;
            if (set.next()) {
                pay = String.valueOf(set.getDouble(2));
            }
            map.put("total", pay == null ? "Completed" : pay);
            map.put("paymentId", txtPaymentId.getText());
            map.put("paid", txtAmount.getText());
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnCloseOnAction(MouseEvent mouseEvent) {
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
    }

    public void cmb_Key_Released(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            try {
                if (OrderCrudController.getOrderIds().contains(cmbOrderId.getValue())) {
                    txtAmount.requestFocus();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtAmount.clear();
        cmbOrderId.getSelectionModel().clearSelection();
    }
}
