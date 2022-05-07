package controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;
import util.OptionButtonsUtil;
import util.UIStyleUtil;
import views.tms.ItemTM;
import views.tms.PaymentTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class PaymentsFormController {
    public TableView<PaymentTM> tblPayment;
    public TextField txtSearch;

    public void initialize() {
        tblPayment.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("paymentId"));
        tblPayment.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("orderId"));
        tblPayment.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("amount"));
        tblPayment.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("date"));
        tblPayment.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("time"));
        tblPayment.getColumns().get(5).setCellValueFactory(param -> {
            ImageView delete = OptionButtonsUtil.setDeleteButton();
            ImageView edit = OptionButtonsUtil.setEditButton();

            delete.setOnMouseClicked((MouseEvent event) -> {
                if (tblPayment.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {
                        Object object = tblPayment.getSelectionModel().getSelectedItem();
                        PaymentTM tm = (PaymentTM) object;
                        try {
                            CrudUtil.execute("DELETE FROM Payment WHERE id=?", tm.getPaymentId());
                            TrayNotification tray = new TrayNotification("Deleted", "Successful", NotificationType.SUCCESS);
                            tray.showAndDismiss(Duration.millis(2000));
                            tblPayment.setItems(PaymentsCrudController.loadAllPayments());
                            tblPayment.refresh();
                        } catch (SQLException | ClassNotFoundException e) {
                            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                        }
                    }
                }
            });
            return new ReadOnlyObjectWrapper(new HBox(20, edit, delete));
        });
        try {
            tblPayment.setItems(PaymentsCrudController.loadAllPayments());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnAddPaymentOnAction(ActionEvent actionEvent) throws IOException {
        UIStyleUtil.setPopUpWindow("AddPayment");
        initialize();
    }

    public void txtSearchOnAction(KeyEvent keyEvent) {
        try {
            tblPayment.setItems(PaymentsCrudController.loadSearchedPayments(txtSearch.getText()));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
