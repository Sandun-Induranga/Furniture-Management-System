package controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.UIStyleUtil;

import java.io.IOException;


public class HomeFormController {
    public Circle c1;
    public Circle c2;
    public Circle c3;
    public Circle c4;
    public Circle c5;
    public Circle c6;
    public Circle c7;
    public Circle c8;
    public Circle c9;
    public Circle c10;
    public AnchorPane context;
    public FontAwesomeIconView btnMin;

    public void initialize() {
        setAnimation(c1, 900);
        setAnimation(c2, 1000);
        setAnimation(c3, 1050);
        setAnimation(c4, 1100);
        setAnimation(c5, 1150);
        setAnimation(c6, 1200);
        setAnimation(c7, 1250);
        setAnimation(c8, 1300);
        setAnimation(c9, 1350);
        setAnimation(c10, 1400);
    }

    private void setAnimation(Circle c, int seconds) {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setNode(c);
        translateTransition.setDuration(Duration.millis(seconds));
        translateTransition.setCycleCount(TranslateTransition.INDEFINITE);
        translateTransition.setByY(-30);
        translateTransition.setAutoReverse(true);
        translateTransition.play();
    }

    public void closeOnAction(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void btnAdminOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AdminLoginForm");
    }

    public void btnUserOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserLoginForm");
    }

    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../views/" + location + ".fxml")));
        UIStyleUtil.setStageStyle(scene, context);
    }

    public void btnMinusOnAction(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnMin.getScene().getWindow();
        stage.setIconified(true);
    }
}
