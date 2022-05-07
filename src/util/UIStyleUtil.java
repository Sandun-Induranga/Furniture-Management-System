package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class UIStyleUtil {
    public static void setStageStyle(Scene scene, AnchorPane anchorPane) {
        anchorPane.getChildren().clear();
        scene.setFill(Color.TRANSPARENT);
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        setDrag(scene, stage);
    }

    public static void setDrag(Scene scene, Stage stage) {
        final double[] xOffset = {0};
        final double[] yOffset = {0};
        scene.setOnMousePressed(event -> {
            xOffset[0] = event.getSceneX();
            yOffset[0] = event.getSceneY();
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset[0]);
            stage.setY(event.getScreenY() - yOffset[0]);
        });
    }

    public static void setPopUpWindow(String location) throws IOException {
        Stage stage = new Stage();
        Scene scene = new Scene(FXMLLoader.load(UIStyleUtil.class.getResource("../views/"+location+".fxml")));
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.centerOnScreen();
        UIStyleUtil.setDrag(scene, stage);
        stage.showAndWait();
    }
}
