package javaFXFrame;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static javafx.scene.Cursor.*;
import static javafx.scene.Cursor.DEFAULT;
import static javafx.scene.input.MouseEvent.*;

/**
 * Provides the basic controls of a desktop application window such as
 * resizing, repositioning, minimizing, maximizing and closing.
 * The class is designed for use with undecorated JavaFX stages.
 * @author Bjarke Brodin Larsen
 * @version 28/03/2020
 */
public class ApplicationWindowController {
    private Stage stage;
    private Scene scene;
    private Rectangle window;
    private Rectangle offset;
    private double offsetX;
    private double offsetY;

    public void initialize(Stage stage, Scene scene, Node minimize, Node maximize, Node close) {
        this.stage = stage;
        this.scene = scene;

        offset = new Rectangle(stage.getX(),stage.getY(),stage.getWidth(),stage.getHeight());
        window = new Rectangle();

        bindStageDimensions();

        for (var button : (new Node[]{minimize, maximize, close})) {
            button.setStyle(button.getStyle() + "-fx-cursor: hand;");
        }

        minimize.addEventHandler(MOUSE_CLICKED, this::handleMinimizeButton);
        maximize.addEventHandler(MOUSE_CLICKED, this::handleMaximizeButton);
        close.addEventHandler(MOUSE_CLICKED, this::closeWindow);

        stage.addEventHandler(MOUSE_PRESSED, this::handleMouseClicked);
        stage.addEventHandler(MOUSE_MOVED, this::checkMousePosition);
        stage.addEventHandler(MOUSE_DRAGGED, this::handleDragged);
    }

    private void handleMinimizeButton(MouseEvent e) {
        stage.setIconified(true);
    }

    private void handleMaximizeButton(MouseEvent e) {
        stage.setMaximized(!stage.isMaximized());
    }

    private void closeWindow(MouseEvent e) {
        Platform.exit();
        System.exit(0);
    }

    private void handleDragged(MouseEvent e) {
        if (scene.getCursor() != DEFAULT)
            handleResize(e);
        else
            handleMove(e);
    }

    private void handleMove(MouseEvent e) {
        window.setX(e.getScreenX() - offsetX);
        window.setY(e.getScreenY() - offsetY);
    }

    private void handleMouseClicked(MouseEvent e) {
        offsetX = e.getSceneX();
        offsetY = e.getSceneY();
        offset.setX(stage.getX());
        offset.setY(stage.getY());
        offset.setWidth(stage.getWidth());
        offset.setHeight(stage.getHeight());
    }

    private void checkMousePosition(MouseEvent e) {
        var x = e.getSceneX();
        var y = e.getSceneY();
        var w = stage.getWidth();
        var h = stage.getHeight();

        double borderWidth = 5;
        if      ( 0 <= x && x <= borderWidth && 0 <= y && y <= borderWidth)
            scene.setCursor(NW_RESIZE);
        else if ( 0 <= x && x <= borderWidth && h - borderWidth <= y && y < h )
            scene.setCursor(SW_RESIZE);
        else if ( w - borderWidth <= x && x <= w && 0 <= y && y <= borderWidth)
            scene.setCursor(NE_RESIZE);
        else if ( w - borderWidth <= x && x <= w && h - borderWidth <= y && y <= h )
            scene.setCursor(SE_RESIZE);
        else if ( 0 <= x && x <= borderWidth && 0 <= y && y <= h )
            scene.setCursor(W_RESIZE);
        else if ( w- borderWidth <= x && x <= w && 0 <= y && y <= h )
            scene.setCursor(E_RESIZE);
        else if ( 0 <= x && x <= w && 0 <= y && y <= borderWidth)
            scene.setCursor(N_RESIZE);
        else if ( 0 <= x && x <= w && h - borderWidth <= y && y <= h )
            scene.setCursor(S_RESIZE);
        else
            scene.setCursor(DEFAULT);
    }

    private void handleResize(MouseEvent e) {
        if      ( scene.getCursor() == SE_RESIZE )
            resizeSouthEast(e);
        else if ( scene.getCursor() == NE_RESIZE )
            resizeNorthEast(e);
        else if ( scene.getCursor() == NW_RESIZE )
            resizeNorthWest(e);
        else if ( scene.getCursor() == SW_RESIZE )
            resizeSouthWest(e);
        else if ( scene.getCursor() == E_RESIZE )
            resizeEast(e);
        else if ( scene.getCursor() == S_RESIZE )
            resizeSouth(e);
        else if ( scene.getCursor() == W_RESIZE )
            resizeWest(e);
        else if ( scene.getCursor() == N_RESIZE )
            resizeNorth(e);
    }

    private void resizeSouth(MouseEvent e) {
        window.setHeight(e.getScreenY() - offset.getY());
    }

    private void resizeEast(MouseEvent e) {
        window.setWidth(e.getScreenX() - offset.getX());
    }

    private void resizeNorth(MouseEvent e) {
        if ( e.getScreenY() >= offset.getY() + offset.getHeight() )
            return;

        window.setY(e.getScreenY());
        window.setHeight(offset.getY() + offset.getHeight() - e.getScreenY());
    }

    private void resizeWest(MouseEvent e) {
        if ( e.getScreenX() >= offset.getX() + offset.getWidth() )
            return;

        window.setX(e.getScreenX());
        window.setWidth(offset.getX() + offset.getWidth() - e.getScreenX());
    }

    private void resizeNorthEast(MouseEvent e) {
        resizeNorth(e);
        resizeEast(e);
    }

    private void resizeSouthEast(MouseEvent e) {
        resizeSouth(e);
        resizeEast(e);
    }

    private void resizeNorthWest(MouseEvent e) {
        resizeNorth(e);
        resizeWest(e);
    }

    private void resizeSouthWest(MouseEvent e) {
        resizeSouth(e);
        resizeWest(e);
    }

    private void bindStageDimensions() {
        window.widthProperty().addListener((a,b,c) -> {
            stage.setWidth(c.doubleValue());
        });
        window.heightProperty().addListener((a,b,c) -> {
            stage.setHeight(c.doubleValue());
        });
        window.xProperty().addListener((a,b,c) -> {
            stage.setX(c.doubleValue());
        });
        window.yProperty().addListener((a,b,c) -> {
            stage.setY(c.doubleValue());
        });
        stage.widthProperty().addListener((a,b,c) -> {
            window.setWidth(c.doubleValue());
        });
        stage.heightProperty().addListener((a,b,c) -> {
            window.setHeight(c.doubleValue());
        });
        stage.xProperty().addListener((a,b,c) -> {
            window.setX(c.doubleValue());
        });
        stage.yProperty().addListener((a,b,c) -> {
            window.setY(c.doubleValue());
        });
    }
}