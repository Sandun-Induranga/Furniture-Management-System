package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.ClockUtil;
import util.UIStyleUtil;

import java.io.IOException;

public class UserDashboardFormController {
    public JFXButton lblClock;
    public JFXButton lblDate;
    public AnchorPane viewContext;

    public void initialize(){
        ClockUtil.startClock(lblDate,lblClock);
    }

    public void btnMakePaymentOnAction(ActionEvent actionEvent) throws IOException {
        UIStyleUtil.setPopUpWindow("AddPayment");
    }

    public void btnAddCustomerOnAction(ActionEvent actionEvent) throws IOException {
        UIStyleUtil.setPopUpWindow("AddCustomerForm");
    }

    public void btnItemListOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminItemForm");
    }

    public void btnOrderOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminItemForm");
    }

    private void setUi(String location) throws IOException {
        viewContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../views/" + location + ".fxml"));
        viewContext.getChildren().add(parent);
    }
}
