package vinneg.natpoacher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.security.NoSuchAlgorithmException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws NoSuchAlgorithmException {
        Worker worker = new Worker();
        Thread workerThread = new Thread(worker, "WorkerThread");

        // Заголовок окна
        primaryStage.setX(890);
        primaryStage.setY(240);
        primaryStage.setTitle("Nat Poacher");
        primaryStage.setOpacity(0.4); // непрозрачность
        primaryStage.initStyle(StageStyle.UTILITY);

        // Создаём контейнер для элементов (вертикальная компоновка)
        VBox root = new VBox(10); // 10 — отступ между элементами
        root.setStyle("-fx-padding: 20; -fx-background-color: #f0f0f0;");

        // Создаём первую кнопку
        Button button1 = new Button("FISH");
        button1.setPrefSize(120, 40);
        button1.setOnAction(e -> {
            int x = (int) primaryStage.getX();
            int y = (int) primaryStage.getY();
            int width = (int) primaryStage.getWidth();
            int height = (int) primaryStage.getHeight();

            System.out.println("win " + x + "-" + y + " " + width + "-" + height);

            try {
                worker.clicker = new Clicker(x, y, width, height);
                workerThread.start();
            } catch (AWTException ex) {
            }
        });

        // Создаём вторую кнопку
        Button button2 = new Button("STOP");
        button2.setPrefSize(120, 40);
        button2.setOnAction(e -> {
            try {
                workerThread.interrupt();
                workerThread.join(3 * 60 * 1_000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Добавляем кнопки в контейнер
        root.getChildren().addAll(button1, button2);

        // Создаём сцену с контейнером
        Scene scene = new Scene(root, 780, 240);

        // Устанавливаем сцену в окно
        primaryStage.setScene(scene);

        // Показываем окно
        primaryStage.show();
    }

    static void main(String[] args) {
        launch(args);
    }

}
