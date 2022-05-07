package controllers;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
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
import views.tms.ItemTM;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class AdminItemFormController {

    public TableView<ItemTM> tblItem;
    public TableColumn colCode;
    public TableColumn colDes;
    public TableColumn colCategory;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public ComboBox<String> cmbCategory;
    public TextField txtSearch;

    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory("code"));
        colCategory.setCellValueFactory(new PropertyValueFactory("category"));
        colDes.setCellValueFactory(new PropertyValueFactory("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory("qtyOnHand"));
        tblItem.getColumns().get(5).setCellValueFactory(param -> {
            ImageView delete = OptionButtonsUtil.setDeleteButton();
            ImageView edit = OptionButtonsUtil.setEditButton();

            delete.setOnMouseClicked((MouseEvent event) -> {
                if (tblItem.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {
                        Object object = tblItem.getSelectionModel().getSelectedItem();
                        ItemTM tm = (ItemTM) object;
                        try {
                            CrudUtil.execute("DELETE FROM Item WHERE code=?", tm.getCode());
                            tblItem.setItems(ItemCrudController.loadAllItem());
                            tblItem.refresh();
                            TrayNotification tray = new TrayNotification("Deleted", "Successful", NotificationType.SUCCESS);
                            tray.showAndDismiss(Duration.millis(2000));
                        } catch (SQLException | ClassNotFoundException e) {
                            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                        }
                    }
                }
            });

            edit.setOnMouseClicked((MouseEvent event) -> {
                AddItemFormController.isEditButton = true;
                try {
                    UIStyleUtil.setPopUpWindow("AddItemForm");
                    tblItem.setItems(ItemCrudController.loadAllItem());
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return new ReadOnlyObjectWrapper(new HBox(20, edit, delete));
        });
        try {
            tblItem.setItems(ItemCrudController.loadAllItem());
            cmbCategory.getItems().setAll(CategoryFormController.getCategoryName());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData(newValue.getCode());
            }
        });

        cmbCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                try {
                    tblItem.setItems(ItemCrudController.loadFilteredItems(newValue));
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setData(String code) {
        ItemTM tm = null;
        try {
            tm = ItemCrudController.getItem(code);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (tm != null) {
            AddItemFormController.code = tm.getCode();
            AddItemFormController.category = tm.getCategory();
            AddItemFormController.description = tm.getDescription();
            AddItemFormController.price = tm.getUnitPrice();
            AddItemFormController.qty = tm.getQtyOnHand();
        }
    }

    public void btnAddItemOnAction(ActionEvent actionEvent) throws IOException {
        UIStyleUtil.setPopUpWindow("AddItemForm");
        try {
            tblItem.setItems(ItemCrudController.loadAllItem());
            tblItem.refresh();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnAddCategoryOnAction(ActionEvent actionEvent) throws IOException {
        UIStyleUtil.setPopUpWindow("CategoryForm");
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) {
        initialize();
    }

    public void txtSearchOnAction(KeyEvent keyEvent) {
        try {
            tblItem.setItems(ItemCrudController.loadSearchedItem(txtSearch.getText()));
            tblItem.refresh();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
