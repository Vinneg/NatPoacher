package vinneg.natpoacher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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

    private double mainX;
    private double mainY;

    private Stage slave;
    private Stage magni;
    private Stage over;

    @Override
    public void start(Stage main) {
        var overPane = new StackPane();
        overPane.setOpacity(0.1);

        Scene overScene = new Scene(overPane, Color.TRANSPARENT);
        overScene.setFill(Color.TRANSPARENT);

        overScene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            int x = (int) event.getScreenX();
            int y = (int) event.getScreenY();

            try {
                Robot robot = new Robot();

                magni.hide();
                over.hide();

                Optional.of(new java.awt.Rectangle(x - Seeker.R, y - Seeker.R, SIDE, SIDE))
                        .map(robot::createScreenCapture)
                        .ifPresent(Seeker::define);
            } catch (AWTException _) {
            }
        });

        over = new Stage();
        over.initStyle(StageStyle.TRANSPARENT);
        over.setFullScreen(true);
        over.setAlwaysOnTop(true);
        over.setScene(overScene);

        Optional<String> ext = Optional.of(getClass())
                .map(v -> v.getResource("/style.css"))
                .map(URL::toExternalForm);

        Circle dot = new Circle(SIDE * 2, SIDE * 2, 3, Color.AZURE);
        Rectangle rack = new Rectangle(SIDE, SIDE, SIDE * 2 + 1, SIDE * 2 + 1);
        rack.setFill(null);
        rack.setStroke(Color.AZURE);
        rack.setStrokeWidth(1);

        ImageView img = new ImageView();
        img.setFitWidth(SIDE * 4 + 1);
        img.setFitHeight(SIDE * 4 + 1);
        img.setPreserveRatio(true);

        Pane magniRoot = new Pane();
        magniRoot.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        magniRoot.getChildren().addAll(img, dot, rack);

        Scene magniScene = new Scene(magniRoot, SIDE * 4 + 1, SIDE * 4 + 1);
        ext.ifPresent(magniScene.getStylesheets()::add);

        magni = new Stage(StageStyle.UNDECORATED);
        magni.setScene(magniScene);
        magni.setWidth(SIDE * 4 + 1);
        magni.setHeight(SIDE * 4 + 1);
        magni.setAlwaysOnTop(true);

        magniRoot.setOnMousePressed(e -> {
            magniX = e.getScreenX() - magni.getX();
            magniY = e.getScreenY() - magni.getY();
        });
        magniRoot.setOnMouseDragged(e -> {
            magni.setX(e.getScreenX() - magniX);
            magni.setY(e.getScreenY() - magniY);
        });

        slave = new Stage();
        slave.setTitle("Secondary Window");
        slave.initStyle(StageStyle.UTILITY);
        slave.initOwner(main);
        slave.setX(1370);
        slave.setY(160);
        slave.setOpacity(0.4);
        slave.setWidth(700);
        slave.setHeight(500);

        VBox root = new VBox(10);

        root.setOnMousePressed(e -> {
            mainX = e.getScreenX() - main.getX();
            mainY = e.getScreenY() - main.getY();
        });
        root.setOnMouseDragged(e -> {
            main.setX(e.getScreenX() - mainX);
            main.setY(e.getScreenY() - mainY);
        });

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
                over.show();

                try {
                    Magnifier.start(magni, img);
                } catch (AWTException _) {
                    magni.hide();
                    over.hide();
                    Magnifier.stop();
                }
            } else {
                magni.hide();
                over.hide();
                Magnifier.stop();
            }
        });

        Button close = new Button("close");
        close.setPrefSize(120, 20);
        close.setOnAction(_ -> {
            slave.close();
            magni.close();
            over.close();
            main.close();
            Worker.stop();
            Magnifier.stop();
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

    @Override
    public void stop() {
        slave.close();
        magni.close();
        over.close();
        Worker.stop();
        Magnifier.stop();
    }

    static void main(String[] args) {
        launch(args);
    }

}
