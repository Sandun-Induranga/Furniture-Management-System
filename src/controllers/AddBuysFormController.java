package controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;
import util.OptionButtonsUtil;
import util.PrimaryKeyUtil;
import views.tms.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddBuysFormController {
    public Label lblProducts;
    public Label lblMaterials;
    public Label lblTopic;
    public ComboBox<String> cmbItemCode;
    public AnchorPane context;
    public ComboBox<String> cmbSupId;
    public JFXTextField txtSupName;
    public Label lblTotalCost;
    public JFXTextField txtDes;
    public JFXTextField txtQty;
    public JFXTextField txtPrice;
    public TableView<OwnerCartTm> tblCart;
    public JFXTextField txtAddress;
    public JFXTextField txtPhoneNumber;
    String id;

    public void initialize() {
        try {
            cmbItemCode.getItems().addAll(ItemCrudController.loadAllItemCodes());
            cmbSupId.getItems().addAll(SupplierCrudController.loadAllSupplierIds());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setItemDetails(newValue);
        });
        cmbSupId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setSuppliers(newValue);
            cmbItemCode.setDisable(false);
        });

        tblCart.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("code"));
        tblCart.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("description"));
        tblCart.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("qty"));
        tblCart.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("cost"));
        tblCart.getColumns().get(4).setCellValueFactory(param -> {
            ImageView delete = OptionButtonsUtil.setDeleteButton();
            delete.setOnMouseClicked(event -> {
                if (!tmList.isEmpty()) {
                    for (OwnerCartTm tm :
                            tmList) {
                        if (tm.getCode().equals(id)) {
                            tmList.remove(tm);
                            calculateTotal();
                            break;
                        }
                    }
                }
            });
            return new ReadOnlyObjectWrapper(new HBox(20, delete));
        });
        tblCart.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                id = newValue.getCode();
            }
        });
        cmbItemCode.setDisable(true);
    }

    private void setItemDetails(String code) {
        try {
            ItemTM item = ItemCrudController.getItem(code);
            if (item != null) {
                txtQty.clear();
                txtQty.setStyle("-fx-prompt-text-fill: #A7A7A7");
                txtDes.setText(item.getDescription());
                txtPrice.setText(String.valueOf(item.getUnitPrice()));
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result", ButtonType.OK);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setSuppliers(String id) {
        try {
            SupplierTM tm = SupplierCrudController.getSupplier(id);
            if (tm != null) {
                txtSupName.setText(tm.getName());
                txtAddress.setText(tm.getAddress());
                txtPhoneNumber.setText(tm.getPhoneNumber());
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result", ButtonType.OK);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void lblProductsOnAction(MouseEvent mouseEvent) {
        lblProducts.setStyle("-fx-text-fill: #FFC42A");
        lblMaterials.setStyle("-fx-text-fill: #A7A7A7");
        cmbItemCode.setDisable(false);
    }

    public void lblMaterialOnAction(MouseEvent mouseEvent) {
        lblMaterials.setStyle("-fx-text-fill: #FFC42A");
        lblProducts.setStyle("-fx-text-fill: #A7A7A7");
        cmbItemCode.setDisable(true);
        cmbItemCode.getSelectionModel().clearSelection();
    }

    public void closeOnAction(MouseEvent mouseEvent) {
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
    }

    public void btnBackOnAction(MouseEvent mouseEvent) throws IOException {
        context.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../views/AdminBuysForm.fxml"));
        context.getChildren().add(parent);
    }

    public void btnCheckOutOnAction(ActionEvent actionEvent) {
        try {
            String id = PrimaryKeyUtil.setBuyId();
            BuyTM tm = new BuyTM(
                    id,
                    cmbSupId.getValue(),
                    txtSupName.getText(),
                    LocalDate.now().toString(),
                    Double.parseDouble(lblTotalCost.getText())
            );

            BuysCrudController.addBuys(tm);
            for (OwnerCartTm ownerCartTm :
                    tmList) {
                if (!ownerCartTm.getCode().equals("-")) {
                    CrudUtil.execute("INSERT INTO SupplyDetail VALUES (?,?,?,?)",
                            id, ownerCartTm.getCode(), ownerCartTm.getQty(), ownerCartTm.getCost()
                    );
                    CrudUtil.execute("UPDATE Item SET qtyOnHand=qtyOnHand+? WHERE code=?", ownerCartTm.getQty(), ownerCartTm.getCode());
                    TrayNotification tray = new TrayNotification("Saved", "Successful", NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.millis(2000));
                } else {
                    CrudUtil.execute("INSERT INTO MaterialDetail VALUES (?,?,?,?)",
                            id, ownerCartTm.getDescription(), ownerCartTm.getQty(), ownerCartTm.getCost()
                    );
                    TrayNotification tray = new TrayNotification("Saved", "Successful", NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.millis(2000));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    ObservableList<OwnerCartTm> tmList = FXCollections.observableArrayList();

    public void btnAddToCartOnAction(ActionEvent actionEvent) {

        if (!txtQty.getText().isEmpty()) {
            int qty = Integer.parseInt(txtQty.getText());
            for (OwnerCartTm tm :
                    tmList) {
                if (tm.getCode().equals(cmbItemCode.getValue())) {
                    tm.setQty(qty + tm.getQty());
                    tblCart.refresh();
                    calculateTotal();
                    return;
                }
            }
            OwnerCartTm tm;
            ImageView btn = OptionButtonsUtil.setDeleteButton();
            if (cmbItemCode.getSelectionModel().isEmpty()) {
                tm = new OwnerCartTm(
                        "-",
                        txtDes.getText(),
                        qty,
                        Double.parseDouble(txtPrice.getText())
                );
            } else {
                tm = new OwnerCartTm(
                        cmbItemCode.getValue(),
                        txtDes.getText(),
                        qty,
                        Double.parseDouble(txtPrice.getText())
                );
            }

            btn.setOnMouseClicked(e -> {
                for (OwnerCartTm tempTm :
                        tmList) {
                    if (tempTm.getCode().equals(tm.getCode())) {
                        tmList.remove(tempTm);
                        calculateTotal();
                    }
                }
            });
            tmList.add(tm);
            tblCart.setItems(tmList);
            tblCart.refresh();
            calculateTotal();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please Select Quantity").show();
        }
    }

    private void calculateTotal() {
        double total = 0;
        for (OwnerCartTm tm :
                tmList) {
            total += tm.getCost();
        }
        lblTotalCost.setText(String.valueOf(total));
    }
}
