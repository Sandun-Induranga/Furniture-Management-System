package controllers;

import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import util.OptionButtonsUtil;
import views.tms.MaterialTM;
import views.tms.BuyTM;
import views.tms.SupplyDetailTM;

import java.io.IOException;
import java.sql.SQLException;

public class AdminBuysFormController {
    public TableView<BuyTM> tblBuys;
    public TableView<SupplyDetailTM> tblItemBuys;
    public TableView<MaterialTM> tblMaterial;
    public AnchorPane context;
    public TextField txtSearch;
    public JFXDatePicker txtBegin;
    public JFXDatePicker txtEnd;

    public void initialize() {
        tblBuys.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("orderId"));
        tblBuys.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("supId"));
        tblBuys.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("supName"));
        tblBuys.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("orderDate"));
        tblBuys.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("cost"));
        tblBuys.getColumns().get(5).setCellValueFactory(param -> {
            ImageView delete = OptionButtonsUtil.setDeleteButton();
            delete.setOnMouseClicked((MouseEvent event) -> {
                try {
                    tblBuys.setItems(BuysCrudController.deleteBuys(tblBuys));
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return new ReadOnlyObjectWrapper(new HBox(20, delete));
        });

        tblItemBuys.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("code"));
        tblItemBuys.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("qty"));
        tblItemBuys.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("price"));

        tblMaterial.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("des"));
        tblMaterial.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("qty"));
        tblMaterial.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("price"));

        try {
            tblBuys.setItems(BuysCrudController.loadAllBuys());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblBuys.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(newValue);
        });

    }

    private void setData(BuyTM tm) {
        if (tm != null) {
            try {
                tblItemBuys.setItems(BuysCrudController.loadBuyDetails(tm));
                tblMaterial.setItems(BuysCrudController.loadMaterialDetails(tm));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnAddBuyOnAction(ActionEvent actionEvent) throws IOException {
        context.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../views/AddBuysForm.fxml"));
        context.getChildren().add(parent);
    }

    public void txtSearchOnAction(KeyEvent keyEvent) {
        try {
            tblBuys.setItems(BuysCrudController.loadSearchedBuys(txtSearch.getText()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnFilterOnAction(ActionEvent actionEvent) {
        try {
            tblBuys.setItems(BuysCrudController.loadFilteredBuys(txtBegin.getValue().toString(), txtEnd.getValue().toString()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
