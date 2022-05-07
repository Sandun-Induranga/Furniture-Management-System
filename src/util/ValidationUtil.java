package util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ValidationUtil {
    public static Object validate(LinkedHashMap<JFXTextField, Pattern> map, JFXButton btn) {
        for (JFXTextField key : map.keySet()) {
            Pattern pattern = map.get(key);
            if (!pattern.matcher(key.getText()).matches()) {
                addError(key, btn);
                return key;
            }
            removeError(key, btn);
        }
        return true;
    }

    public static void removeError(JFXTextField textField, JFXButton btn) {
        textField.setStyle("-fx-text-fill: #CDCDCD; -fx-font-size: 20; -fx-prompt-text-fill: #CDCDCD");
        btn.setDisable(false);
    }

    public static void addError(JFXTextField textField, JFXButton btn) {
        textField.setStyle("-fx-text-fill: #9b2226; -fx-font-size: 20; -fx-prompt-text-fill: #CDCDCD");
        btn.setDisable(true);
    }
}
