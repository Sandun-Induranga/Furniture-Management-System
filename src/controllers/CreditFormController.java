package controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;
import util.OptionButtonsUtil;
import util.UIStyleUtil;
import views.tms.CreditTM;
import views.tms.ItemTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class CreditFormController {
    public TableView<CreditTM> tblCredit;
    public TextField txtSearch;
    public JFXComboBox<String> cmbFilter;

    public void initialize() {
        tblCredit.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("orderId"));
        tblCredit.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("cusId"));
        tblCredit.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("cusName"));
        tblCredit.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("amount"));
        tblCredit.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("dueDate"));
        tblCredit.getColumns().get(5).setCellValueFactory(param -> {
            ImageView delete = OptionButtonsUtil.setDeleteButton();
            delete.setOnMouseClicked((MouseEvent event) -> {
                if (tblCredit.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {
                        Object object = tblCredit.getSelectionModel().getSelectedItem();
                        CreditTM tm = (CreditTM) object;
                        try {
                            CrudUtil.execute("DELETE FROM Credit WHERE orderId=?", tm.getOrderId());
                            tblCredit.setItems(CreditCrudController.loadAllCredits());
                            tblCredit.refresh();
                            TrayNotification tray = new TrayNotification("Deleted", "Successful", NotificationType.SUCCESS);
                            tray.showAndDismiss(Duration.millis(2000));
                        } catch (SQLException | ClassNotFoundException e) {
                            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                        }
                    }
                }
            });
            return new ReadOnlyObjectWrapper(new HBox(20, delete));
        });
        try {
            tblCredit.setItems(CreditCrudController.loadAllCredits());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        cmbFilter.getItems().setAll("Expired");
        cmbFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                try {
                    tblCredit.setItems(CreditCrudController.loadFilteredCredits());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void btnAddPaymentOnAction(ActionEvent actionEvent) throws IOException {
        UIStyleUtil.setPopUpWindow("AddPayment");
        initialize();
    }

    public void txtSearchOnAction(KeyEvent keyEvent) {
        try {
            tblCredit.setItems(CreditCrudController.loadSearchedCredits(txtSearch.getText()));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) {
        initialize();
    }
}
