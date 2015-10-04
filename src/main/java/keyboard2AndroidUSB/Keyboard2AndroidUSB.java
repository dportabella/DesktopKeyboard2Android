package keyboard2AndroidUSB;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/*
How can I predict whether a key_pressed event will be followed by a key_typed event?
http://stackoverflow.com/questions/32897191/how-can-i-predict-whether-a-key-pressed-event-will-be-followed-by-a-key-typed-ev

I didn't find doc about this;
it seems that I cannot decide based on keyCode classes (function, navigation, arrow...)
I've implemented this logic based on experimenting.

- note that LAST_CHAR_AS_CONTROL_EVENT < LAST_KEY_CODE_AS_CONTROL_EVENT!
  for instance, typing the Up arrow, produces the KeyCode.UP = 0x26, while typing the char '&' also produces the char 0x26;

- note for instance, the enter (0x0A), the backspace (0x08), the space (0x20) keys produce both a key_pressed/key_released and a key_typed event.
*/
public class Keyboard2AndroidUSB extends Application {
    final int LAST_KEY_CODE_AS_CONTROL_EVENT = KeyCode.DOWN.impl_getCode(); // 0x28
    final int LAST_CHAR_AS_CONTROL_EVENT = (int) ' '; // 0x20

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Keyboard");
        Scene scene = new Scene(new Label(), 300, 275);
        primaryStage.setScene(scene);

        scene.setOnKeyPressed((KeyEvent e) -> {
            if (handleAsControlEvent(e.getCode())) {
                System.out.println("key_pressed, code: " + keyCodeToString(e.getCode()));
            }
        });

        scene.setOnKeyReleased((KeyEvent e) -> {
            if (handleAsControlEvent(e.getCode())) {
                System.out.println("key_released, code: " + keyCodeToString(e.getCode()));
            }
        });

        scene.setOnKeyTyped((KeyEvent e) -> {
            if (handleAsTypedEvent(e.getCharacter())) {
                System.out.println("key_typed, char: " + e.getCharacter());
            }
        });

        primaryStage.show();
    }

    private boolean handleAsControlEvent(KeyCode keyCode) {
        return (keyCode.impl_getCode() <= LAST_KEY_CODE_AS_CONTROL_EVENT);
    }

    private boolean handleAsTypedEvent(String c) {
        return (c.length() > 0 && (c.length() > 1 || c.charAt(0) > LAST_CHAR_AS_CONTROL_EVENT));
    }

    private String keyCodeToString(KeyCode keyCode) {
        StringBuffer sp = new StringBuffer();
        sp.append("[name: " + keyCode.getName() + ", code: " + keyCode.impl_getCode() + ", char: " + keyCode.impl_getChar() + "]");
        return sp.toString();
    }

    public static void main(String[] args) { launch(args); }
}
