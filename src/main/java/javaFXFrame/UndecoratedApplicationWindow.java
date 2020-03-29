package javaFXFrame;

import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static com.sun.jna.platform.win32.WinUser.GWL_STYLE;

public class UndecoratedApplicationWindow {
    public static void initializeAndShow(Stage stage, Scene scene, Node minimize, Node maximize, Node close) {
        unDecorate(stage);
        setController(stage,scene,minimize,maximize,close);

        if (Platform.isWindows() || Platform.isWindowsCE())
            emulateKeyShortcuts(stage);

        showAndFixMinimizeOnIconClick(stage);
    }

    private static void showAndFixMinimizeOnIconClick(Stage stage) {
        stage.show();

        if (!Platform.isWindows() && !Platform.isWindowsCE())
            return;

        long lhwnd = com.sun.glass.ui.Window.getWindows().get(0).getNativeWindow();
        Pointer lpVoid = new Pointer(lhwnd);
        WinDef.HWND hwnd = new WinDef.HWND(lpVoid);
        final User32 user32 = User32.INSTANCE;
        int oldStyle = user32.GetWindowLong(hwnd, GWL_STYLE);
        int newStyle = oldStyle | 0x00020000;
        user32.SetWindowLong(hwnd, GWL_STYLE, newStyle);
    }

    private static void emulateKeyShortcuts(Stage stage) {

    }

    private static void setController(Stage stage, Scene scene, Node minimize, Node maximize, Node close) {
        (new ApplicationWindowController()).initialize(stage,scene,minimize,maximize,close);
    }

    private static void unDecorate(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED);
    }
}