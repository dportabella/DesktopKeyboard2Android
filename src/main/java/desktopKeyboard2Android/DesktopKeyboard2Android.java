package desktopKeyboard2Android;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import java.util.List;

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
    TextArea infoWindow;

    final int LAST_KEY_CODE_AS_CONTROL_EVENT = KeyCode.DOWN.impl_getCode(); // 0x28
    final int LAST_CHAR_AS_CONTROL_EVENT = (int) ' '; // 0x20

    final int ENTER_KEY_CODE = 10;
    final int ENTER_CODE_POINT = 13;
    final int ENTER_ANDROID_CODE_POINT = 10;

    /* used to store and, if necessary, later forward a key used with modifiers (such as Crtl)
     * that has not been translated to a usefull typed key.
     * For instance:
     *   Crtl-V produces a KeyTyped event that is not forwarded. So lastIgnoredKeyCode will be forwarded.
     *   Alt-g in a Swiss-French keyboard produces a KeyTyped event (@ character) that is forwarded. So lastIgnoredKeyCode will not be forwarded.
     */
    KeyCode lastIgnoredKeyCode = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Keyboard");
        infoWindow = new TextArea();
        infoWindow.setText(instrauctions);
        infoWindow.setEditable(false);
        infoWindow.setDisable(true);    // todo: how to be enabed but stop receiving key events?
        Scene scene = new Scene(infoWindow, 600, 400);
        primaryStage.setScene(scene);

        scene.setOnKeyPressed((KeyEvent e) -> {
            if (handleAsControlEvent(e.getCode())) {
                sendKeyPressed(e.getCode());
                lastIgnoredKeyCode = null;
            } else {
                lastIgnoredKeyCode = e.getCode();
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
            } else if (lastIgnoredKeyCode != null) {
                sendKeyPressed(lastIgnoredKeyCode);
                sendKeyReleased(lastIgnoredKeyCode);
                lastIgnoredKeyCode = null;
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
        addInfo("key_typed, char: " + str);
        int c = str.codePointAt(0);   // todo: is it possible to have more than one?
        int androidChar = (c == ENTER_CODE_POINT) ? ENTER_ANDROID_CODE_POINT : c;
        sendKeyEvent("C" + androidChar);
    }

    private void sendKeyPressed(KeyCode keyCode) {
        addInfo("key_pressed, code: " + keyCodeToString(keyCode));
        sendKeyEvent("D" + keyCode.impl_getCode());
    }

    private void sendKeyReleased(KeyCode keyCode) {
        addInfo("key_released, code: " + keyCodeToString(keyCode));
        sendKeyEvent("U" + keyCode.impl_getCode());
    }

    private void sendKeyEvent(String keyEventString) {
        keySequence++;
        String url = "http://localhost:7777/key?" + keySequence + "," + keyEventString + ",";
        List<String> lines;
        try {
            lines = httpRequest(url);
        } catch (IOException e) {
            addInfo("connection to androidWifiKeyboard app failed");
            return;
        }
        if (lines.size() != 1 || !"ok".equals(lines.get(0))) {
            addInfo("unexpected response from android WifiKeyboard app: " + lines);
        }
    }

    private List<String> httpRequest(String url) throws IOException {
        HttpURLConnection httpc = (HttpURLConnection) new URL(url).openConnection();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(httpc.getInputStream(), StandardCharsets.UTF_8))) {
            return in.lines().collect(Collectors.toList());
        }
    }

    private void addInfo(String text) {
        infoWindow.appendText(text + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }

    final static String instrauctions =
            "DesktopKeyboard2Android\n" +
                    "Use your laptop's keyboard for typing in your Android phone\n" +
                    "https://github.com/dportabella/DesktopKeyboard2Android/\n\n" +
                    "- Install the WifiPassword app: https://play.google.com/store/apps/details?id=com.volosyukivan&hl=en\n" +
                    "- Follow its instructions to connect your laptop and android together through wifi or usb.\n" +
                    "  this includes executing from the terminal: adb forward tcp:7777 tcp:7777\n" +
                    "- Test that it works by browsing this web page from your laptop and type some text: http://localhost:7777/\n" +
                    "- What you type here now, it will be forwarded to your Android\n\n";
}
