package controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import model.Customer;
import util.OptionButtonsUtil;
import util.UIStyleUtil;

import java.io.IOException;
import java.sql.SQLException;

public class AdminCustomerFormController {
    public TableView<Customer> tblCustomer;
    public TableColumn colCustomerId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colEmail;
    public TableColumn colPhoneNumber;
    public TableColumn colOption;
    public AnchorPane context;
    public TextField txtSearch;

    public void initialize() {
        colCustomerId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory("phoneNumber"));
        setButtons();
        try {
            tblCustomer.setItems(CustomerCrudController.loadAllCustomers());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setData(newValue);
        });
    }

    private void setData(Customer customer) {
        if (customer != null) {
            AddCustomerFormController.id = customer.getId();
            AddCustomerFormController.name = customer.getName();
            AddCustomerFormController.address = customer.getAddress();
            AddCustomerFormController.phoneNumber = customer.getPhoneNumber();
        }
    }

    private void setButtons() {
        colOption.setCellValueFactory(param -> {
            ImageView delete = OptionButtonsUtil.setDeleteButton();
            ImageView edit = OptionButtonsUtil.setEditButton();
            delete.setOnMouseClicked((MouseEvent event) -> {
                try {
                    tblCustomer.setItems(CustomerCrudController.deleteCustomer(tblCustomer));
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            edit.setOnMouseClicked(event -> {
                try {
                    AddCustomerFormController.isEditButton = true;
                    UIStyleUtil.setPopUpWindow("AddCustomerForm");
                    tblCustomer.setItems(CustomerCrudController.loadAllCustomers());
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return new ReadOnlyObjectWrapper(new HBox(20, edit, delete));
        });
    }

    public void btnAddCustomerOnAction(ActionEvent actionEvent) throws IOException {
        UIStyleUtil.setPopUpWindow("AddCustomerForm");
        initialize();
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) {
        initialize();
    }

    public void searchCustomer(KeyEvent keyEvent) {
        try {
            tblCustomer.setItems(CustomerCrudController.searchCustomers(txtSearch.getText()));
            tblCustomer.refresh();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
