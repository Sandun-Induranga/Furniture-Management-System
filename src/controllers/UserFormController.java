package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.ClockUtil;
import util.UIStyleUtil;

import java.io.IOException;
import java.util.Optional;

public class UserFormController {
    public AnchorPane context;
    public AnchorPane menuContext;
    public AnchorPane viewContext;
    public JFXButton lblClock;
    public JFXButton lblDate;
    public FontAwesomeIconView btnMin;
    public JFXToggleButton btnDarkMode;
    public AnchorPane topBar;
    public Label lblHeader;
    public Label lblUserName;

    public void initialize() {
        ClockUtil.startClock(lblDate,lblClock);
        if (AdminFormController.isDarkModeActivate) {
            menuContext.setStyle("-fx-background-color: #293241 ; -fx-background-radius: 50 0 0 50;");
            topBar.setStyle("-fx-background-color: #293241 ; -fx-background-radius:  0 50 0 30;");
//            viewContext.setStyle("-fx-background-color: #2e3440");
//            backgroundContext.setStyle("-fx-background-color: #2e3440; -fx-background-radius:  0 50 50 0;");
        } else {
            menuContext.setStyle("-fx-background-color: white ; -fx-background-radius: 50 0 0 50;");
            topBar.setStyle("-fx-background-color: white ; -fx-background-radius:  0 50 0 30;");
//            viewContext.setStyle("-fx-background-color: #F3F3F3");
//            backgroundContext.setStyle("-fx-background-color: F3F3F3; -fx-background-radius:  0 50 50 0;");
        }
        lblUserName.setText(UserLoginFormController.name);
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserDashboardForm", "Dashboard");
    }

    public void btnItemsOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserItemForm", "Items");
    }

    public void btnCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminCustomerForm", "Customers");
    }

    public void btnSalesOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminSalesForm", "Sales");
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) throws IOException {
        setUi("PlaceOrderForm", "Place Order");
    }

    public void btnCreditOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CreditForm", "Credits");
    }

    public void btnReturnOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ReturnForm", "Returns");
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)) {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../views/HomeForm.fxml")));
            UIStyleUtil.setStageStyle(scene, context);
        }
    }

    public void btnPaymentOnAction(ActionEvent actionEvent) throws IOException {
        setUi("PaymentsForm", "Payments");
    }

    private void setUi(String location, String header) throws IOException {
        lblHeader.setText(header);
        viewContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../views/" + location + ".fxml"));
        viewContext.getChildren().add(parent);
    }

    public void btnDarkModeOnAction(ActionEvent actionEvent) {
        AdminFormController.isDarkModeActivate = btnDarkMode.isSelected() ? true : false;
        initialize();
    }

    public void minimizeOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnMin.getScene().getWindow();
        stage.setIconified(true);
    }

    public void closeOnAction(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void btnEditProfileOnAction(ActionEvent actionEvent) throws IOException {
        setUi("EditProfileForm", "Edit Profile");
    }

    public void btnMakePaymentOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../views/AddPayment.fxml")));
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.centerOnScreen();
        UIStyleUtil.setDrag(scene, stage);
        stage.show();
    }

    public void btnAddCustomerOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../views/AddCustomerForm.fxml")));
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.centerOnScreen();
        UIStyleUtil.setDrag(scene, stage);
        stage.show();
    }

    public void btnItemListOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminItemForm","Items");
    }

    public void btnOrderOnAction(ActionEvent actionEvent) throws IOException {
        setUi("PlaceOrderForm","Place Order");
    }
}
