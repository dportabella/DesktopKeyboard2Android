package desktopKeyboard2Android;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class DesktopKeyboard2Android extends Application {
    final static Logger logger = LoggerFactory.getLogger(DesktopKeyboard2Android.class);

    final int LAST_KEY_CODE_AS_CONTROL_EVENT = KeyCode.DOWN.impl_getCode(); // 0x28
    final int LAST_CHAR_AS_CONTROL_EVENT = (int) ' '; // 0x20

    final int ENTER_KEY_CODE = 10;
    final int ENTER_CODE_POINT = 13;
    final int ENTER_ANDROID_CODE_POINT = 10;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Keyboard");
        Scene scene = new Scene(new Label(), 300, 275);
        primaryStage.setScene(scene);

        scene.setOnKeyPressed((KeyEvent e) -> {
            if (handleAsControlEvent(e.getCode())) {
                sendKeyPressed(e.getCode());
            }
        });

        scene.setOnKeyReleased((KeyEvent e) -> {
            if (handleAsControlEvent(e.getCode())) {
                sendKeyReleased(e.getCode());
            }
        });

        scene.setOnKeyTyped((KeyEvent e) -> {
            if (handleAsTypedEvent(e.getCharacter())) {
                sendKeyTyped(e.getCharacter());
            }
        });

        primaryStage.show();
    }

    private boolean handleAsControlEvent(KeyCode keyCode) {
        int code = keyCode.impl_getCode();
        return (code != ENTER_KEY_CODE && code <= LAST_KEY_CODE_AS_CONTROL_EVENT);
    }

    private boolean handleAsTypedEvent(String c) {
        return ( (c.length() == 1 && c.charAt(0) == ENTER_CODE_POINT)  // enter key
                || (c.length() > 0 && (c.length() > 1 || c.charAt(0) > LAST_CHAR_AS_CONTROL_EVENT)));
    }

    private String keyCodeToString(KeyCode keyCode) {
        StringBuffer sp = new StringBuffer();
        sp.append("[name: " + keyCode.getName() + ", code: " + keyCode.impl_getCode() + ", char: " + keyCode.impl_getChar() + "]");
        return sp.toString();
    }

    private int keySequence = 30000;

    private void sendKeyTyped(String str) {
        logger.debug("key_typed, char: " + str);
        int c = str.codePointAt(0);   // todo: is it possible to have more than one?
        int androidChar = (c == ENTER_CODE_POINT) ? ENTER_ANDROID_CODE_POINT : c;
        sendKeyEvent("C" + androidChar);
    }

    private void sendKeyPressed(KeyCode keyCode) {
        logger.debug("key_pressed, code: " + keyCodeToString(keyCode));
        sendKeyEvent("D" + keyCode.impl_getCode());
    }

    private void sendKeyReleased(KeyCode keyCode) {
        logger.debug("key_released, code: " + keyCodeToString(keyCode));
        sendKeyEvent("U" + keyCode.impl_getCode());
    }

    private void sendKeyEvent(String keyEventString) {
        keySequence++;
        String url = "http://localhost:7777/key?" + keySequence + "," + keyEventString + ",";
        List<String> lines = httpRequest(url);
        if (lines.size() != 1 || !"ok".equals(lines.get(0))) {
            exit(new RuntimeException("unexpected response from android WifiKeyboard app: " + lines));
        }
    }

    private List<String> httpRequest(String url) {
        try {
            HttpURLConnection httpc = (HttpURLConnection) new URL(url).openConnection();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(httpc.getInputStream(), StandardCharsets.UTF_8))) {
                return in.lines().collect(Collectors.toList());
            }
        } catch (IOException e) {
            exit(new RuntimeException("connection to androidWifiKeyboard app failed", e));
            return null;
        }
    }

    private void exit(Throwable e) {
        logger.error("exiting", e);
        System.exit(1);

    }
    public static void main(String[] args) {
        launch(args);
    }
}
