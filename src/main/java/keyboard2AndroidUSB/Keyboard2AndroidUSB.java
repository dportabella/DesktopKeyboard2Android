package keyboard2AndroidUSB;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Keyboard2AndroidUSB extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Keyboard");
        Scene scene = new Scene(new Label(), 300, 275);
        primaryStage.setScene(scene);

        scene.setOnKeyPressed((KeyEvent e) -> System.out.println("key_pressed, code: " + keyCodeToString(e.getCode())));
        scene.setOnKeyReleased((KeyEvent e) -> System.out.println("key_released, code: " + keyCodeToString(e.getCode())));
        scene.setOnKeyTyped((KeyEvent e) -> System.out.println("key_typed, char: " + e.getCharacter()));

        primaryStage.show();
    }

    String keyCodeToString(KeyCode keyCode) {
        StringBuffer sp = new StringBuffer();
        sp.append("[name: " + keyCode.getName() + ", code: " + keyCode.impl_getCode() + ", char: " + keyCode.impl_getChar() + ", class: ");
        if (keyCode.isFunctionKey()) { sp.append(" function"); }
        if (keyCode.isNavigationKey()) { sp.append(" navigation"); }
        if (keyCode.isArrowKey()) { sp.append(" arrow"); }
        if (keyCode.isModifierKey()) { sp.append(" modifier"); }
        if (keyCode.isLetterKey()) { sp.append(" letter"); }
        if (keyCode.isDigitKey()) { sp.append(" digit"); }
        if (keyCode.isKeypadKey()) { sp.append(" keypad"); }
        if (keyCode.isWhitespaceKey()) { sp.append(" whitespace"); }
        if (keyCode.isMediaKey()) { sp.append(" media"); }
        sp.append("]");
        return sp.toString();
    }

    public static void main(String[] args) { launch(args); }
}
