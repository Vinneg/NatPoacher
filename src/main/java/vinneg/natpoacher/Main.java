package vinneg.natpoacher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Заголовок окна
        primaryStage.setTitle("Nat Poacher");
        primaryStage.setOpacity(0.3); // непрозрачность
        primaryStage.initStyle(StageStyle.UTILITY);

        // Создаём контейнер для элементов (вертикальная компоновка)
        VBox root = new VBox(10); // 10 — отступ между элементами
        root.setStyle("-fx-padding: 20; -fx-background-color: #f0f0f0;");

        // Создаём первую кнопку
        Button button1 = new Button("Кнопка 1");
        button1.setPrefSize(120, 40);
        button1.setOnAction(e -> {
            primaryStage.close(); // Закрывает текущее окно
        });

        // Создаём вторую кнопку
        Button button2 = new Button("Кнопка 2");
        button2.setPrefSize(120, 40);
        button2.setOnAction(e -> {
            System.out.println("Нажата Кнопка 2!");
        });

        // Добавляем кнопки в контейнер
        root.getChildren().addAll(button1, button2);

        // Создаём сцену с контейнером
        Scene scene = new Scene(root, 300, 200);

        // Устанавливаем сцену в окно
        primaryStage.setScene(scene);

        // Показываем окно
        primaryStage.show();
    }

    static void main(String[] args) {
        launch(args);
    }
}
