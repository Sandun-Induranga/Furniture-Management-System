package controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import views.tms.ItemTM;

import java.sql.SQLException;

public class UserItemFormController {
    public TableView<ItemTM> tblItem;
    public ComboBox<String> cmbCategory;
    public TextField txtSearch;

    public void initialize(){
        tblItem.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("code"));
        tblItem.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("category"));
        tblItem.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("description"));
        tblItem.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("unitPrice"));
        tblItem.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("qtyOnHand"));

        try {
            tblItem.setItems(ItemCrudController.loadAllItem());
            cmbCategory.getItems().setAll(CategoryFormController.getCategoryName());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
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

    public void txtSearchOnAction(KeyEvent keyEvent) {
        try {
            tblItem.setItems(ItemCrudController.loadSearchedItem(txtSearch.getText()));
            tblItem.refresh();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) {
        initialize();
    }
}
