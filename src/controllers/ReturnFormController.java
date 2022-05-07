package controllers;

import com.jfoenix.controls.JFXDatePicker;
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
import util.CrudUtil;
import util.OptionButtonsUtil;
import views.tms.ReturnTM;

import java.sql.SQLException;
import java.util.Optional;

public class ReturnFormController {

    public TableView<ReturnTM> tblReturn;
    public TextField txtSearch;
    public JFXDatePicker txtBegin;
    public JFXDatePicker txtEnd;

    public void initialize(){
        tblReturn.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("id"));
        tblReturn.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("code"));
        tblReturn.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("description"));
        tblReturn.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("reason"));
        tblReturn.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("date"));
        tblReturn.getColumns().get(5).setCellValueFactory(new PropertyValueFactory("amount"));
        tblReturn.getColumns().get(6).setCellValueFactory(new PropertyValueFactory("qty"));
        tblReturn.getColumns().get(7).setCellValueFactory(param -> {
            ImageView delete = OptionButtonsUtil.setDeleteButton();
            ImageView edit = OptionButtonsUtil.setEditButton();

            delete.setOnMouseClicked((MouseEvent event) -> {
                if (tblReturn.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {
                        Object object = tblReturn.getSelectionModel().getSelectedItem();
                        ReturnTM tm = (ReturnTM) object;
                        try {
                            CrudUtil.execute("DELETE FROM `Return` WHERE orderId=?", tm.getId());
                            new Alert(Alert.AlertType.INFORMATION, "SUCCESS").show();
                            tblReturn.setItems(ReturnCrudController.loadAllReturns());
                            tblReturn.refresh();
                        } catch (SQLException | ClassNotFoundException e) {
                            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                        }
                    }
                }
            });
            return new ReadOnlyObjectWrapper(new HBox(20, edit, delete));
        });
        try {
            tblReturn.setItems(ReturnCrudController.loadAllReturns());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void txtSearchOnAction(KeyEvent keyEvent) {
        try {
            tblReturn.setItems(ReturnCrudController.loadSearchedReturns(txtSearch.getText()));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnFilterOnAction(ActionEvent actionEvent) {
        try {
            tblReturn.setItems(ReturnCrudController.loadFilteredReturns(txtBegin.getValue().toString(),txtEnd.getValue().toString()));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) {
        initialize();
    }
}
