package desktopKeyboard2Android;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ShowJavaKeyEvents extends Application {
    final int LAST_KEY_CODE_AS_CONTROL_EVENT = KeyCode.DOWN.impl_getCode(); // 0x28
    final int LAST_CHAR_AS_CONTROL_EVENT = (int) ' '; // 0x20

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Keyboard");
        Scene scene = new Scene(new Label(), 300, 275);
        primaryStage.setScene(scene);

        scene.setOnKeyPressed((KeyEvent e) -> {
            System.out.print("D[" + keyCodeToString(e.getCode()) + "] ");
        });

        scene.setOnKeyReleased((KeyEvent e) -> {
            System.out.print("U[" + keyCodeToString(e.getCode()) + "] ");
            if (e.getCode().impl_getCode() == 27) { System.out.println(); }
        });

        scene.setOnKeyTyped((KeyEvent e) -> {
            System.out.print("C[" + keyCharToString(e) + "] ");
        });

        primaryStage.show();
    }

    private String keyCodeToString(KeyCode keyCode) {
        return keyCode.getName() + ":" + keyCode.impl_getCode();
    }

    private String keyCharToString(KeyEvent e) {
        String c = e.getCharacter();
        String cStr = c.replaceAll("\r", "");
        String codePointStr = (c.length() == 0) ? "(none)" : String.valueOf(c.codePointAt(0));
        return cStr + ":" + codePointStr;
    }

    public static void main(String[] args) { launch(args); }
}
