package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import model.Category;
import util.CrudUtil;
import util.OptionButtonsUtil;
import util.PrimaryKeyUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class CategoryFormController {
    public TableView tblCategory;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colOptions;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXButton btnAddCategory;
    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colOptions.setCellValueFactory(param -> {
            ImageView delete = OptionButtonsUtil.setDeleteButton();
            ImageView edit = OptionButtonsUtil.setEditButton();

            delete.setOnMouseClicked((MouseEvent event) -> {
                if (tblCategory.getSelectionModel().getSelectedItem() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get().equals(ButtonType.YES)) {
                        Object object = tblCategory.getSelectionModel().getSelectedItem();
                        Category c = (Category)object;
                        try {
                            CrudUtil.execute("DELETE FROM Category WHERE id=?", c.getId());
                            new Alert(Alert.AlertType.INFORMATION, "SUCCESS").show();
                            tblCategory.setItems(loadAllCategory(tblCategory));
                            tblCategory.refresh();
                        } catch (SQLException | ClassNotFoundException e) {
                            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                        }
                    }
                }
            });

            edit.setOnMouseClicked((MouseEvent event)->{
                setEditButton();
            });
            return new ReadOnlyObjectWrapper(new HBox(20, edit, delete));
        });
        try {
            loadAllCategory(tblCategory);
            txtId.setText(PrimaryKeyUtil.setCategoryId());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Pattern namePattern = Pattern.compile("[A-z0-9 ,/]{3,}");

        map.put(txtName, namePattern);

        if (txtId.getText().isEmpty() || txtName.getText().isEmpty()) {
            btnAddCategory.setDisable(true);
        }
        txtName.requestFocus();
    }

    public void setEditButton(){
        int i =0 ;
        if (tblCategory.getSelectionModel().getSelectedItem()!=null){
            Object object = tblCategory.getSelectionModel().getSelectedItem();
            Category category = (Category) object;
            ArrayList<String> arrayList = new ArrayList();
            arrayList.add(category.getId());
            arrayList.add(category.getName());
            for (JFXTextField textField : map.keySet()) {
                textField.setText(arrayList.get(i));
                i++;
            }
        }
    }

    public static ObservableList<Category> loadAllCategory(TableView tableView) throws SQLException, ClassNotFoundException {
        ObservableList<Category> obList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Category";
        ResultSet result = CrudUtil.execute(sql);
        while (result.next()) {
            obList.add(new Category(
                    result.getString(1),
                    result.getString(2)
            ));
        }
        tableView.setItems(obList);
        return obList;
    }

    private void clearAllTexts() {
        txtId.clear();
        txtName.clear();
    }

    public void btnCloseOnAction(MouseEvent mouseEvent) {
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
    }

    public void txtFieldsKeyReleased(KeyEvent keyEvent) {
        ValidationUtil.validate(map, btnAddCategory);

        // Enter key on action
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = ValidationUtil.validate(map, btnAddCategory);

            if (response instanceof JFXTextField) {
                JFXTextField textField = (JFXTextField) response;
                textField.requestFocus();
                btnAddCategory.setDisable(true);
            } else if (response instanceof Boolean) {
                System.out.println("work");
            }
        }
    }

    public void btnAddCategoryOnAction(ActionEvent actionEvent) {
        try {
            if (CrudUtil.execute("INSERT INTO Category VALUES (?,?)", txtId.getText(), txtName.getText())) {
                new Alert(Alert.AlertType.INFORMATION, "SUCCESS").show();
                loadAllCategory(tblCategory);
                clearAllTexts();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public static ObservableList<String> getCategoryName() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT name FROM Category");
        ObservableList<String> observableList = FXCollections.observableArrayList();
        while (resultSet.next()){
            observableList.add(resultSet.getString(1));
        }
        return observableList;
    }
}
