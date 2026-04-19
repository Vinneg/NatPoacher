package vinneg.natpoacher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.security.NoSuchAlgorithmException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws NoSuchAlgorithmException {
        // Заголовок окна
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setTitle("Nat Poacher");
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setX(1800);
        primaryStage.setY(220);

        // Создаём контейнер для элементов (вертикальная компоновка)
        VBox root = new VBox(10); // 10 — отступ между элементами
        root.setStyle("-fx-padding: 20; -fx-background-color: #f0f0f0;");

        // Дополнительное окно (создаём заранее, но не показываем)
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Secondary Window");
        secondaryStage.initStyle(StageStyle.UTILITY);
        secondaryStage.initOwner(primaryStage);
        secondaryStage.setX(870);
        secondaryStage.setY(80);
        secondaryStage.setOpacity(0.4); // непрозрачность
        secondaryStage.setWidth(800);
        secondaryStage.setHeight(400);

        // Создаём первую кнопку
        Button button1 = new Button("FISH");
        button1.setPrefSize(120, 40);
        button1.setOnAction(e -> {
            int x = (int) secondaryStage.getX();
            int y = (int) secondaryStage.getY();
            int width = (int) secondaryStage.getWidth();
            int height = (int) secondaryStage.getHeight();

            System.out.println("win " + x + "-" + y + " " + width + "-" + height);

            try {
                Worker.start(new Clicker(x, y, width, height));
            } catch (AWTException | NoSuchAlgorithmException ignore) {
            }
        });

        // Создаём вторую кнопку
        Button button2 = new Button("STOP");
        button2.setPrefSize(120, 120);
        button2.setOnAction(e -> Worker.stop());

        primaryStage.setOnCloseRequest(e -> Worker.stop());

        // Создаём toggle‑кнопку для управления дополнительным окном
        ToggleButton toggleWindowButton = new ToggleButton("OPEN WINDOW");
        toggleWindowButton.setPrefSize(120, 40);

        // Обработчик для toggle‑кнопки
        toggleWindowButton.setOnAction(event -> {
            if (toggleWindowButton.isSelected()) {
                // При первом нажатии — открываем окно
                toggleWindowButton.setText("CLOSE WINDOW");
                secondaryStage.show();
            } else {
                // При втором нажатии — закрываем окно
                toggleWindowButton.setText("OPEN WINDOW");
                secondaryStage.hide(); // hide() вместо close() — окно можно будет открыть снова
            }
        });

        // Добавляем кнопки в контейнер
        root.getChildren().addAll(button1, button2, toggleWindowButton);

        // Создаём сцену с контейнером
        Scene scene = new Scene(root);

        // Устанавливаем сцену в окно
        primaryStage.setScene(scene);

        // Показываем окно
        primaryStage.show();
    }

    static void main(String[] args) {
        launch(args);
    }

}
