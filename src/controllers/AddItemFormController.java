package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;
import util.PrimaryKeyUtil;
import util.ValidationUtil;
import views.tms.ItemTM;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class AddItemFormController {
    public JFXTextField txtCode;
    public JFXComboBox<String> cmbCategory;
    public JFXTextField txtDes;
    public JFXTextField txtPrice;
    public JFXTextField txtQty;
    public static String code;
    public static String category;
    public static String description;
    public static double price;
    public static int qty;
    public static boolean isEditButton = false;
    public JFXButton btnSave;
    LinkedHashMap<JFXTextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() {
        try {
            cmbCategory.setEditable(true);
            cmbCategory.getItems().addAll(CategoryFormController.getCategoryName());
            TextFields.bindAutoCompletion(cmbCategory.getEditor(), cmbCategory.getItems());
            cmbCategory.requestFocus();
            cmbCategory.setStyle("-fx-text-fill: #A7A7A7; -fx-prompt-text-fill: #A7A7A7");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (!isEditButton) {
            try {
                txtCode.setText(PrimaryKeyUtil.getItemCode());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            txtDes.requestFocus();
        } else {
            btnSave.setText("UPDATE");
            txtCode.setText(code);
            try {
                ResultSet resultSet = CrudUtil.execute("SELECT name FROM Category WHERE id=?",category);
                if (resultSet.next()){
                    cmbCategory.getSelectionModel().select(resultSet.getString(1));
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            txtDes.setText(description);
            txtPrice.setText(String.valueOf(price));
            txtQty.setText(String.valueOf(qty));
        }
        btnSave.setDisable(true);

        // Validate patterns
        Pattern desPattern = Pattern.compile("[A-z0-9 ,/]{2,}");
        Pattern pricePattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");
        Pattern qtyPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");

        map.put(txtDes, desPattern);
        map.put(txtQty, qtyPattern);
        map.put(txtPrice, pricePattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map, btnSave);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = ValidationUtil.validate(map, btnSave);

            if (response instanceof JFXTextField) {
                JFXTextField textField = (JFXTextField) response;
                textField.setStyle("-fx-prompt-text-fill: #A7A7A7");
                textField.requestFocus();
            } else if (response instanceof Boolean) {
                saveItem();
                ((Node) (keyEvent.getSource())).getScene().getWindow().hide();
            }
        }
    }

    private void clearAllTexts() {
        cmbCategory.getSelectionModel().clearSelection();
        txtDes.clear();
        txtPrice.clear();
        txtQty.clear();
    }

    public void btnCloseOnAction(MouseEvent mouseEvent) {
        isEditButton = false;
        ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        saveItem();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    private void saveItem() {
        if (btnSave.getText().equals("SAVE")) {
            ItemTM tm = new ItemTM(txtCode.getText(),
                    cmbCategory.getValue(),
                    txtDes.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQty.getText()));
            ItemCrudController.addItem(tm);
            TrayNotification tray = new TrayNotification("Saved", "Successful", NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(2000));
        } else {
            ItemTM tm = new ItemTM(txtCode.getText(),
                    cmbCategory.getValue(),
                    txtDes.getText(),
                    Double.parseDouble(txtPrice.getText()),
                    Integer.parseInt(txtQty.getText()));
            editItem(tm);
            isEditButton = false;
        }
    }

    public void editItem(ItemTM itemTM) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)) {
            try {
                ResultSet resultSet = CrudUtil.execute("SELECT * FROM Category WHERE name=?", itemTM.getCategory());
                if (resultSet.next()) {
                    itemTM.setCategory(resultSet.getString(1));
                }
                CrudUtil.execute("UPDATE Item SET code=?,categoryId=?,description=?,unitPrice=?,qtyOnHand=? WHERE code=?",
                        itemTM.getCode(),
                        itemTM.getCategory(),
                        itemTM.getDescription(),
                        itemTM.getUnitPrice(),
                        itemTM.getQtyOnHand(),
                        txtCode.getText()
                );
                TrayNotification tray = new TrayNotification("Edited", "Successful", NotificationType.SUCCESS);
                tray.showAndDismiss(Duration.millis(2000));
                isEditButton = false;
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearAllTexts();
    }
}
