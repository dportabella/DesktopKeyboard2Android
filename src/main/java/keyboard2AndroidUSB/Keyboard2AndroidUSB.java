package keyboard2AndroidUSB;

import javafx.application.Application;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Keyboard2AndroidUSB extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Keyboard");
        Scene scene = new Scene(label, 300, 275);
        primaryStage.setScene(scene);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                System.out.println("hello: " + e);
            }
        });

        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
