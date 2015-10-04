package keyboard2AndroidUSB;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Keyboard2AndroidUSB extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Keyboard");
        Scene scene = new Scene(new Label(), 300, 275);
        primaryStage.setScene(scene);

        scene.setOnKeyPressed((KeyEvent e) -> System.out.println("key_pressed, code: " + e.getCode()));
        scene.setOnKeyReleased((KeyEvent e) -> System.out.println("key_released, code: " + e.getCode()));
        scene.setOnKeyTyped((KeyEvent e) -> System.out.println("key_typed, char: " + e.getCharacter()));

        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}
