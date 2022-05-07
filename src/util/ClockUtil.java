package util;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ClockUtil {
    public static void startClock(JFXButton lblDate, JFXButton lblClock) {
        lblDate.setText(new SimpleDateFormat("yyyy - MM - dd").format(new Date()));
        Timeline clock = new Timeline(new KeyFrame(Duration.millis(1000 - Calendar.getInstance().get(Calendar.MILLISECOND)), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Calendar cal = Calendar.getInstance();
                int second = cal.get(Calendar.SECOND);
                int minute = cal.get(Calendar.MINUTE);
                int hour = cal.get(Calendar.HOUR);
                hour = hour == 0 ? 12 : hour;
                String am_pm = (cal.get(Calendar.AM_PM) == 0) ? "AM" : "PM";
                lblClock.setText(String.format("%02d : %02d : %02d %s", hour, minute, second, am_pm));
            }
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}
