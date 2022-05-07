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
import views.tms.SupplierTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class AdminSupplierFormController {

    public TableView<SupplierTM> tblSupplier;
    public TextField txtSearch;

    public void initialize() {
        tblSupplier.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("id"));
        tblSupplier.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("name"));
        tblSupplier.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("description"));
        tblSupplier.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("address"));
        tblSupplier.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("email"));
        tblSupplier.getColumns().get(5).setCellValueFactory(new PropertyValueFactory("phoneNumber"));
        tblSupplier.getColumns().get(6).setCellValueFactory(param -> {
            ImageView delete = OptionButtonsUtil.setDeleteButton();
            ImageView edit = OptionButtonsUtil.setEditButton();
            delete.setOnMouseClicked((MouseEvent event) -> {
                if (tblSupplier.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {
                        Object object = tblSupplier.getSelectionModel().getSelectedItem();
                        SupplierTM tm = (SupplierTM) object;
                        try {
                            CrudUtil.execute("DELETE FROM Supplier WHERE id=?", tm.getId());
                            TrayNotification tray = new TrayNotification("Deleted", "Successful", NotificationType.SUCCESS);
                            tray.showAndDismiss(Duration.millis(2000));
                            tblSupplier.setItems(SupplierCrudController.loadAllSupplier());
                            tblSupplier.refresh();
                        } catch (SQLException | ClassNotFoundException e) {
                            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                        }
                    }
                }
            });
            edit.setOnMouseClicked(event -> {
                try {
                    AddSupplierFormController.isEditButton = true;
                    UIStyleUtil.setPopUpWindow("AddSupplierForm");
                    initialize();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return new ReadOnlyObjectWrapper(new HBox(20, edit, delete));
        });
        try {
            tblSupplier.setItems(SupplierCrudController.loadAllSupplier());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        tblSupplier.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(newValue);
        });
    }

    private void setData(SupplierTM tm) {
        if (tm != null) {
            AddSupplierFormController.isEditButton = true;
            AddSupplierFormController.id = tm.getId();
            AddSupplierFormController.name = tm.getName();
            AddSupplierFormController.des = tm.getDescription();
            AddSupplierFormController.address = tm.getAddress();
            AddSupplierFormController.email = tm.getEmail();
            AddSupplierFormController.phoneNumber = tm.getPhoneNumber();
        }
    }

    public void btnAddSupplierOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        UIStyleUtil.setPopUpWindow("AddSupplierForm");
        tblSupplier.setItems(SupplierCrudController.loadAllSupplier());
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) {
        initialize();
    }

    public void txtSearchOnAction(KeyEvent keyEvent) {
        try {
            tblSupplier.setItems(SupplierCrudController.loadSearchedSupplier(txtSearch.getText()));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
