package javaFXFrame;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WindowControlButton extends Label {

    public WindowControlButton (int size, Color color, Stage stage) {
        var backgroundActive = new Background(new BackgroundFill(color.darker(), new CornerRadii(100), new Insets(0,0,0,0)));
        var backgroundInactive = new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(100), new Insets(0,0,0,0)));
        var backgroundHovered = new Background(new BackgroundFill(color.brighter(), new CornerRadii(100), new Insets(0,0,0,0)));

        setBackground(backgroundActive);

        addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
            setBackground(backgroundHovered);
        });

        addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
            setBackground(backgroundActive);
        });

        stage.focusedProperty().addListener((a,b,c) -> {
            if (c) setBackground(backgroundActive);
            else setBackground(backgroundInactive);
        });

        setStyle(getStyle() +
                "-fx-pref-height: "+ size + ";" +
                "-fx-pref-width: "+ size +";" +
                "-fx-min-height: "+ size +";" +
                "-fx-min-width: "+ size +";" +
                "-fx-max-height: "+ size +";" +
                "-fx-max-width: "+ size +";");
    }
}