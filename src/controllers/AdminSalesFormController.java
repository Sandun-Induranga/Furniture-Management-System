package controllers;

import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import model.Customer;
import util.OptionButtonsUtil;
import util.UIStyleUtil;
import views.tms.ItemDetailTM;
import views.tms.SaleTM;

import java.io.IOException;
import java.sql.SQLException;

public class AdminSalesFormController {

    public TableView<SaleTM> tblSales;
    public TableView<ItemDetailTM> tblDetails;
    public Label lblId;
    public Label lblName;
    public Label lblAddress;
    public Label lblPhoneNumber;
    public JFXDatePicker txtBeginDate;
    public JFXDatePicker txtEndDate;
    public TextField txtSearch;

    public void initialize() {
        tblSales.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("orderId"));
        tblSales.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("cusId"));
        tblSales.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("date"));
        tblSales.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("time"));
        tblSales.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("cost"));
        tblSales.getColumns().get(5).setCellValueFactory(new PropertyValueFactory("status"));
        tblSales.getColumns().get(6).setCellValueFactory(param -> {
            ImageView delete = OptionButtonsUtil.setDeleteButton();
            delete.setOnMouseClicked((MouseEvent event) -> {
                try {
                    tblSales.setItems(SalesCrudController.deleteOrder(tblSales));
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return new ReadOnlyObjectWrapper(new HBox( delete));
        });
        try {
            tblSales.setItems(SalesCrudController.loadAllSales());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        tblDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("code"));
        tblDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("qty"));
        tblDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("price"));
        tblDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("discount"));

        tblSales.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(newValue);
        });
    }

    private void setData(SaleTM tm) {
        if (tm!=null){
            try {
                tblDetails.setItems(SalesCrudController.loadItemDetails(tm.getOrderId()));
                Customer customer = CustomerCrudController.getCustomer(tm.getCusId());
                lblId.setText(customer.getId());
                lblName.setText(customer.getName());
                lblAddress.setText(customer.getAddress());
                lblPhoneNumber.setText(customer.getPhoneNumber());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnReturnOnAction(ActionEvent actionEvent) throws IOException {
        UIStyleUtil.setPopUpWindow("AddReturnForm");
    }

    public void btnFilterOnAction(ActionEvent actionEvent) {
        try {
            tblSales.setItems(SalesCrudController.loadFilteredSales(txtBeginDate.getValue().toString(),txtEndDate.getValue().toString()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void txtSearchOnAction(KeyEvent keyEvent) {
        try {
            tblSales.setItems(SalesCrudController.loadSearchedSales(txtSearch.getText()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) {
        initialize();
    }
}
