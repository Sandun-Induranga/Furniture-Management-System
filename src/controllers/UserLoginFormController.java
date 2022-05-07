package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;
import util.CrudUtil;
import util.UIStyleUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLoginFormController {
    public AnchorPane context;
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public Label lblUsrError;
    public Label lblPasswordError;
    public static String userName;
    public static String name;
    public JFXButton btnLogin;

    public void initialize(){
        btnLogin.setDisable(true);
    }

    public void txtUserNameOnAction(KeyEvent keyEvent) {
        txtUserName.setStyle("-fx-font-size: 16; -fx-prompt-text-fill: #A7A7A7");
        if (keyEvent.getCode() == KeyCode.ENTER) {
            txtPassword.requestFocus();
        }
    }

    public void txtPasswordOnAction(KeyEvent keyEvent) {
        txtPassword.setStyle("-fx-font-size: 16; -fx-prompt-text-fill: #A7A7A7");
        if (!txtPassword.getText().isEmpty()){
            btnLogin.setDisable(false);
        }
        if (keyEvent.getCode() == KeyCode.ENTER) {
            login();
        }
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        login();
    }

    private void login(){
        lblUsrError.setText("");
        lblPasswordError.setText("");
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM User WHERE username=? && role=?",txtUserName.getText(),"User");
            if (resultSet.next()){
                if (resultSet.getString(3).equals(txtPassword.getText())){
                    userName = resultSet.getString(1);
                    name=resultSet.getString(2);
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../views/UserForm.fxml")));
                    UIStyleUtil.setStageStyle(scene, context);
                    TrayNotification tray = new TrayNotification("Sign Up", "Successful", NotificationType.SUCCESS);
                    tray.showAndDismiss(Duration.millis(2000));
                }else {
                    lblPasswordError.setText("Wrong Password. Please try again");
                }
            }else {
                lblUsrError.setText("Wrong username. Please try again");
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public void closeOnAction(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void btnBackOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("HomeForm");
    }

    public void signUpOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserSignUpForm");
    }

    private void setUi(String location) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../views/" + location + ".fxml")));
        UIStyleUtil.setStageStyle(scene, context);
    }
}
