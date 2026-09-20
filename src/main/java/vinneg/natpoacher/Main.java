package vinneg.natpoacher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.security.NoSuchAlgorithmException;

public class Main extends Application {

    @Override
    public void start(Stage main) {
        Stage slave = new Stage();
        slave.setTitle("Secondary Window");
        slave.initStyle(StageStyle.UTILITY);
        slave.initOwner(main);
        slave.setX(1370);
        slave.setY(160);
        slave.setOpacity(0.4);
        slave.setWidth(700);
        slave.setHeight(500);

        VBox root = new VBox(10);

        Label title = new Label("Nat Poacher");
        title.setPrefSize(120, 20);

        ToggleButton start = new ToggleButton("START");
        start.setPrefSize(120, 120);
        start.setOnAction(_ -> {
            if (start.isSelected()) {
                start.setText("STOP");

                int x = (int) slave.getX();
                int y = (int) slave.getY();
                int width = (int) slave.getWidth();
                int height = (int) slave.getHeight();

                System.out.println("win " + x + "-" + y + " " + width + "-" + height);

                try {
                    Worker.start(new Clicker(x, y, width, height));
                } catch (AWTException | NoSuchAlgorithmException ignore) {
                }
            } else {
                start.setText("START");

                Worker.stop();
            }
        });

        ToggleButton aim = new ToggleButton("AIM");
        aim.setPrefSize(120, 40);
        aim.setOnAction(_ -> {
            if (aim.isSelected()) {
                slave.show();
            } else {
                slave.hide();
            }
        });

        Button close = new Button("close");
        close.setPrefSize(120, 20);
        close.setOnAction(_ -> {
            slave.close();
            main.close();
            Worker.stop();
        });

        root.getChildren().addAll(title, start, aim, close);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        main.initStyle(StageStyle.UNDECORATED);
        main.setAlwaysOnTop(true);
        main.setResizable(false);
        main.setX(2300);
        main.setY(650);
        main.setScene(scene);

        main.setOnCloseRequest(_ -> Worker.stop());

        main.show();
    }

    static void main(String[] args) {
        launch(args);
    }

}
