package controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
import model.User;
import util.OptionButtonsUtil;
import util.UIStyleUtil;
import views.tms.ItemTM;

import java.io.IOException;
import java.sql.SQLException;

public class AdminUsersFormController {

    public TableView<User> tblUser;
    public TextField txtSearch;

    public void initialize(){
        tblUser.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("username"));
        tblUser.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("name"));
        tblUser.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("password"));
        tblUser.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("role"));
        tblUser.getColumns().get(4).setCellValueFactory(param -> {
            ImageView delete = OptionButtonsUtil.setDeleteButton();
            ImageView edit = OptionButtonsUtil.setEditButton();
            delete.setOnMouseClicked((MouseEvent event) -> {
                try {
                    tblUser.setItems(UserCrudController.deleteUser(tblUser));
                } catch (SQLException |ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            edit.setOnMouseClicked((MouseEvent event) -> {
                AddUserFormController.isEditButton = true;
                try {
                    UIStyleUtil.setPopUpWindow("AddUserForm");
                    tblUser.setItems(UserCrudController.loadAllUsers());
                    AddUserFormController.isEditButton = false;
                } catch (IOException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return new ReadOnlyObjectWrapper(new HBox(20, edit, delete));
        });
        try {
            ObservableList<User> obList = UserCrudController.loadAllUsers();
            tblUser.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        tblUser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setData(newValue.getUsername());
            }
        });
    }

    private void setData(String username) {
        User tm = null;
        try {
            tm = UserCrudController.getUser(username);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (tm != null) {
            AddUserFormController.name=tm.getName();
            AddUserFormController.userName=tm.getUsername();
            AddUserFormController.password=tm.getPassword();
            AddUserFormController.role=tm.getRole();
        }
    }

    public void btnAddUserOnAction(ActionEvent actionEvent) throws IOException {
        UIStyleUtil.setPopUpWindow("AddUserForm");
        try {
            tblUser.setItems(UserCrudController.loadAllUsers());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void txtSearchOnAction(KeyEvent keyEvent) {
        try {
            tblUser.setItems(UserCrudController.loadSearchedUsers(txtSearch.getText()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
