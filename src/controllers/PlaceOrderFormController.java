package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import model.Customer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.textfield.TextFields;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.*;
import views.tms.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PlaceOrderFormController {
    public ComboBox<String> cmbCusId;
    public JFXButton lblDate;
    public JFXButton lblTime;
    public ComboBox<String> cmbItemCode;
    public JFXTextField txtCusName;
    public JFXTextField txtAddress;
    public JFXTextField txtPhoneNumber;
    public JFXTextField txtDes;
    public JFXTextField txtQty;
    public JFXTextField txtPrice;
    public JFXTextField txtDiscount;
    public JFXTextField txtQtyOnHand;
    public TableView<CartTM> tblCart;
    public Label lblTotalCost;
    public JFXButton btnAddToCart;
    public JFXButton btnCheckOut;
    public JFXButton btnLoan;
    int qtyOnHand = 0;
    String id;
    ObservableList<CartTM> tmList = FXCollections.observableArrayList();

    public void initialize() {
        ClockUtil.startClock(lblDate, lblTime);

        tblCart.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("code"));
        tblCart.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("description"));
        tblCart.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("unitPrice"));
        tblCart.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("qty"));
        tblCart.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("discount"));
        tblCart.getColumns().get(5).setCellValueFactory(new PropertyValueFactory("cost"));
        tblCart.getColumns().get(6).setCellValueFactory(param -> {
            ImageView delete = OptionButtonsUtil.setDeleteButton();
            delete.setOnMouseClicked(event -> {
                if (!tmList.isEmpty()) {
                    for (CartTM tm :
                            tmList) {
                        if (tm.getCode().equals(id)) {
                            tmList.remove(tm);
                            calculateTotal();
                            break;
                        }
                    }
                }
                if (tblCart.getItems().isEmpty()){
                    btnCheckOut.setDisable(true);
                    btnLoan.setDisable(true);
                }
            });
            return new ReadOnlyObjectWrapper(new HBox(20, delete));
        });

        try {
            cmbCusId.setEditable(true);
            TextFields.bindAutoCompletion(cmbCusId.getEditor(), cmbCusId.getItems().setAll(CustomerCrudController.loadAllCustomerIds()));
            cmbItemCode.setEditable(true);
            TextFields.bindAutoCompletion(cmbItemCode.getEditor(), cmbItemCode.getItems().setAll(ItemCrudController.loadAllItemCodes()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbCusId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setCustomerDetails(newValue);
        });
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setItemDetails(newValue);
        });

        btnCheckOut.setDisable(true);
        btnAddToCart.setDisable(true);
        btnLoan.setDisable(true);

        tblCart.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                id = newValue.getCode();
            }
        });
    }

    private void setCustomerDetails(String id) {
        try {
            Customer customer = CustomerCrudController.getCustomer(id);
            if (customer!=null){
                txtCusName.setText(customer.getName());
                txtAddress.setText(customer.getAddress());
                txtPhoneNumber.setText(customer.getPhoneNumber());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setItemDetails(String code) {
        try {
            ItemTM item = ItemCrudController.getItem(code);
            if (item != null) {
                txtQty.clear();
                txtQty.setStyle("-fx-prompt-text-fill: #A7A7A7");
                txtDiscount.clear();
                txtDes.setText(item.getDescription());
                txtQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
                txtPrice.setText(String.valueOf(item.getUnitPrice()));
                qtyOnHand = item.getQtyOnHand();
                for (CartTM tm :
                        tmList) {
                    if (tm.getCode().equals(cmbItemCode.getValue())) {
                        txtQtyOnHand.setText(String.valueOf(Integer.parseInt(txtQtyOnHand.getText()) - tm.getQty()));
                        return;
                    }
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result", ButtonType.OK);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnCheckOutOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String orderId = setOrderId();
        String paymentId = PrimaryKeyUtil.setPaymentId();
        CustomerOrderTM tm = new CustomerOrderTM(
                orderId,
                cmbCusId.getValue(),
                Double.parseDouble(lblTotalCost.getText()),
                LocalDate.now().toString(),
                LocalTime.now().toString(),
                "PAID"
        );

        ArrayList<OrderDetailTM> details = new ArrayList<>();
        for (CartTM cartTM :
                tmList) {
            details.add(new OrderDetailTM(tm.getOrderId(), cartTM.getCode(), cartTM.getQty(), cartTM.getUnitPrice(), cartTM.getDiscount()));
        }
        Connection connection = null;
        try {
            boolean isOrderSaved = new OrderCrudController().saveOrder(tm);
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            if (isOrderSaved) {
                connection.commit();
                boolean isDetailsSaved = new OrderCrudController().saveOrderDetails(details);
                if (isDetailsSaved) {
                    TrayNotification tray = new TrayNotification("Order Placed", "Successful", NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.millis(2000));
                } else {
                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR, "Error").show();
                }
            } else {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Error").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            connection.setAutoCommit(true);
        }
        PaymentTM paymentTM = new PaymentTM(
                paymentId,
                tm.getOrderId(),
                tm.getCost(),
                tm.getDate(),
                tm.getTime()
        );
        PaymentsCrudController.addPayment(paymentTM);
        try {
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/views/reports/Invoice.jasper"));
            Connection reportConnection = DBConnection.getInstance().getConnection();
            HashMap map = new HashMap();
            map.put("name", txtCusName.getText());
            map.put("address", txtAddress.getText());
            map.put("phoneNumber", txtPhoneNumber.getText());
            map.put("orderId", orderId);
            map.put("total", lblTotalCost.getText());
            map.put("paymentId", paymentId);
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, map, reportConnection);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public void btnAddCustomerOnAction(ActionEvent actionEvent) throws IOException {
        UIStyleUtil.setPopUpWindow("AddCustomerForm");
    }

    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        btnLoan.setDisable(false);
        btnCheckOut.setDisable(false);
        if (!txtQty.getText().isEmpty()) {
            double unitPrice = Double.parseDouble(txtPrice.getText());
            int qty = Integer.parseInt(txtQty.getText());
            double totalCost = unitPrice * qty;
            for (CartTM tm :
                    tmList) {
                if (tm.getCode().equals(cmbItemCode.getValue())) {
                    tm.setCost(tm.getCost() + totalCost);
                    tm.setQty(qty + tm.getQty());
                    tm.setDiscount(txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText()));
                    txtQtyOnHand.setText(String.valueOf(qtyOnHand - tm.getQty()));
                    validateQty();
                    tblCart.refresh();
                    calculateTotal();
                    return;
                }
            }
            CartTM tm = new CartTM(
                    cmbItemCode.getValue(),
                    txtDes.getText(),
                    unitPrice,
                    qty,
                    txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText()),
                    totalCost
            );
            tmList.add(tm);
            tblCart.setItems(tmList);
            tblCart.refresh();
            calculateTotal();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please Select Quantity").show();
        }
    }

    private void validateQty() {
        if (!txtQty.getText().isEmpty()) {
            int newQty = Integer.parseInt(txtQtyOnHand.getText()) - Integer.parseInt(txtQty.getText());
            txtQty.setStyle("-fx-prompt-text-fill: #A7A7A7");
            if (newQty < 0) {
                txtQty.setStyle("-fx-text-fill: #9b2226");
                btnAddToCart.setDisable(true);
            } else if (!cmbCusId.getValue().isEmpty() && !cmbItemCode.getValue().isEmpty()) {
                txtQty.setStyle("-fx-text-fill: #A7A7A7");
                txtQtyOnHand.setText(String.valueOf(newQty));
                btnAddToCart.setDisable(false);
            } else {
                txtQty.setStyle("-fx-text-fill: #A7A7A7");
                txtQtyOnHand.setText(String.valueOf(newQty));
            }
        } else {
            txtQty.setStyle("-fx-prompt-text-fill: #A7A7A7");
            txtQtyOnHand.setText(String.valueOf(qtyOnHand));
            btnAddToCart.setDisable(false);
        }
    }

    private void calculateTotal() {
        double total = 0;
        for (CartTM tm :
                tmList) {
            total += tm.getCost() - tm.getDiscount();
        }
        lblTotalCost.setText(String.valueOf(total));
    }

    private String setOrderId() throws SQLException, ClassNotFoundException {
        String lastId = new SalesCrudController().getOrderId();
        int digits = Integer.parseInt(lastId.substring(4));
        String id = "ORD-" + ++digits;
        System.out.println(id);
        if (id.length() < 6) {
            id = "ORD-00" + digits;
        } else if (id.length() < 7) {
            id = "ORD-0" + digits;
        } else {
            id = "ORD-" + digits;
        }
        return id;
    }

    public void txtQtyOnAction(KeyEvent keyEvent) {
        validateQty();
    }

    public void btnLoanOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        String orderId = setOrderId();
        CustomerOrderTM tm = new CustomerOrderTM(
                orderId,
                cmbCusId.getValue(),
                Double.parseDouble(lblTotalCost.getText()),
                LocalDate.now().toString(),
                LocalTime.now().toString(),
                "PENDING"
        );

        ArrayList<OrderDetailTM> details = new ArrayList<>();
        for (CartTM cartTM :
                tmList) {
            details.add(new OrderDetailTM(tm.getOrderId(), cartTM.getCode(), cartTM.getQty(), cartTM.getUnitPrice(), cartTM.getDiscount()));
        }
        Connection connection = null;
        try {
            boolean isOrderSaved = new OrderCrudController().saveOrder(tm);
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            if (isOrderSaved) {
                connection.commit();
                boolean isDetailsSaved = new OrderCrudController().saveOrderDetails(details);
                if (isDetailsSaved) {
                    TrayNotification tray = new TrayNotification("Order Placed", "Successful", NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.millis(2000));
                } else {
                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR, "Error").show();
                }
            } else {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR, "Error").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        } finally {
            connection.setAutoCommit(true);
        }
        AddLoanFormController.orderId = orderId;
        AddLoanFormController.amount = lblTotalCost.getText();
        UIStyleUtil.setPopUpWindow("AddLoanForm");
    }
}
