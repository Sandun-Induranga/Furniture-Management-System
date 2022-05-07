package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.User;
import util.UIStyleUtil;
import util.ValidationUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class UserSignUpFormController {
    public AnchorPane context;
    public JFXTextField txtName;
    public JFXTextField txtUserName;
    public JFXPasswordField txtPassword;
    public JFXPasswordField txtConfirmPassword;
    public CheckBox checkAgree;
    public Label lblPasswordError;
    public Label lblUserError;
    public JFXButton btnSignUp;

    public void initialize(){
        btnSignUp.setDisable(true);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Pattern namePattern = Pattern.compile("^[A-z ]{3,15}$");
        Pattern usernamePattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern passwordPattern = Pattern.compile("^[A-z0-9 @!#$%^&*()=+?<>,/]{4,20}$");
        Pattern confirmPattern = Pattern.compile("^[A-z0-9 @!#$%^&*()=+?<>,/]{4,20}$");

        if (!namePattern.matcher(txtName.getText()).matches()) {
            txtName.setStyle("-fx-text-fill: #9b2226; -fx-font-size: 20; -fx-prompt-text-fill: #A7A7A7");
            btnSignUp.setDisable(true);
        } else {
            txtName.setStyle("-fx-text-fill: #A7A7A7; -fx-font-size: 20; -fx-prompt-text-fill: #A7A7A7");
            btnSignUp.setDisable(true);
            if (keyEvent.getCode() == KeyCode.ENTER) {
                txtUserName.requestFocus();
            }
        }
        if (!usernamePattern.matcher(txtUserName.getText()).matches()) {
            txtUserName.setStyle("-fx-text-fill: #9b2226; -fx-font-size: 20; -fx-prompt-text-fill: #A7A7A7");
            btnSignUp.setDisable(true);
        } else {
            txtUserName.setStyle("-fx-text-fill: #A7A7A7; -fx-font-size: 20; -fx-prompt-text-fill: #A7A7A7");
            btnSignUp.setDisable(true);
            if (keyEvent.getCode() == KeyCode.ENTER) {
                txtPassword.requestFocus();
            }
        }
        if (!passwordPattern.matcher(txtPassword.getText()).matches()) {
            txtPassword.setStyle("-fx-text-fill: #9b2226; -fx-font-size: 20; -fx-prompt-text-fill: #A7A7A7");
            btnSignUp.setDisable(true);
        } else {
            txtPassword.setStyle("-fx-text-fill: #A7A7A7; -fx-font-size: 20; -fx-prompt-text-fill: #A7A7A7");
            btnSignUp.setDisable(true);
            if (keyEvent.getCode() == KeyCode.ENTER) {
                txtConfirmPassword.requestFocus();
                btnSignUp.setDisable(false);
            }
        }
    }

    public void closeOnAction(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void btnBackOnAction(MouseEvent mouseEvent) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../views/UserLoginForm.fxml")));
        UIStyleUtil.setStageStyle(scene, context);
    }

    public void btnSignUpOnAction(ActionEvent actionEvent) {
        signUp();
    }

    private void signUp(){
        if (!checkAgree.isSelected()){
            new Alert(Alert.AlertType.INFORMATION,"You Must Agree the Privacy Terms").show();
            return;
        }
        if (txtPassword.getText().equals(txtConfirmPassword.getText())) {
            try {
                UserCrudController.addUser(
                        new User(txtUserName.getText(), txtName.getText(), txtPassword.getText(), "User")
                );
                new Alert(Alert.AlertType.INFORMATION, "Success").show();
            } catch (SQLException | ClassNotFoundException e) {
                lblUserError.setText("Username Already Taken");
            }
        } else if (!txtConfirmPassword.getText().isEmpty()){
            txtPassword.clear();
            txtConfirmPassword.clear();
            lblPasswordError.setText("Password Confirmation Failed");
        }
    }

    public void learnMoreOnAction(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(new URI("https://www.iubenda.com/privacy-policy/73635429"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void reEnterKeyReleased(KeyEvent keyEvent) {
        btnSignUp.setDisable(false);
        txtConfirmPassword.setStyle("-fx-text-fill: #CDCDCD");
        if (keyEvent.getCode() == KeyCode.ENTER) {
            signUp();
        }
    }
}
