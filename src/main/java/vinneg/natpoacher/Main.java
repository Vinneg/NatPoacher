package vinneg.natpoacher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static vinneg.natpoacher.Bobber.SIDE;

public class Main extends Application {

    private double magniX;
    private double magniY;

    @Override
    public void start(Stage main) {
        Optional<String> ext = Optional.of(getClass())
                .map(v -> v.getResource("/style.css"))
                .map(URL::toExternalForm);

        Circle dot = new Circle(SIDE, SIDE, 3, Color.AZURE);
        Polygon rack = new Polygon(new int[SIDE], new int[SIDE], 4);

        ImageView img = new ImageView();
        img.setFitWidth(100);
        img.setFitHeight(100);
        img.setPreserveRatio(true);

        Pane magniRoot = new Pane();
        magniRoot.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        magniRoot.getChildren().addAll(dot, rack, img);

        Scene magniScene = new Scene(magniRoot, 100, 100);
        ext.ifPresent(magniScene.getStylesheets()::add);

        Stage magni = new Stage(StageStyle.UNDECORATED);
        magni.setScene(magniScene);
        magni.setWidth(SIDE * 2);
        magni.setHeight(SIDE * 2);
        magni.setOpacity(0.3);
        magni.setAlwaysOnTop(true);

        magniRoot.setOnMousePressed(e -> {
            magniX = e.getScreenX() - magni.getX();
            magniY = e.getScreenY() - magni.getY();
        });
        magniRoot.setOnMouseDragged(e -> {
            magni.setX(e.getScreenX() - magniX);
            magni.setY(e.getScreenY() - magniY);
        });

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

        ToggleButton magnify = new ToggleButton("MAGNIFY");
        magnify.setPrefSize(120, 40);
        magnify.setOnAction(e -> {
            if (magnify.isSelected()) {
                magni.show();
            } else {
                magni.hide();
            }
        });

        Button close = new Button("close");
        close.setPrefSize(120, 20);
        close.setOnAction(_ -> {
            slave.close();
            magni.close();
            main.close();
            Worker.stop();
        });

        root.getChildren().addAll(title, start, aim, magnify, close);

        Scene mainScene = new Scene(root);
        ext.ifPresent(mainScene.getStylesheets()::add);

        main.initStyle(StageStyle.UNDECORATED);
        main.setAlwaysOnTop(true);
        main.setResizable(false);
        main.setX(2300);
        main.setY(650);
        main.setScene(mainScene);

        main.setOnCloseRequest(_ -> Worker.stop());

        main.show();
    }

    static void main(String[] args) {
        launch(args);
    }

}
