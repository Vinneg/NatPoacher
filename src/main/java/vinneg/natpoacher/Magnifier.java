package vinneg.natpoacher;

import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class Magnifier {

    private final Stage stage;
    private final ImageView imageView;
    private final Robot robot;
    private boolean isRunning = false;

    public Magnifier() {
        imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        stage = new Stage(StageStyle.UNDECORATED);
        stage.setWidth(100);
        stage.setHeight(100);
        stage.setOpacity(0.8);
        stage.setAlwaysOnTop(true);

//        Scene scene = new Scene(imageView, Color.TRANSPARENT);
//        scene.setFill(Color.TRANSPARENT);
//        stage.setScene(scene);

        try {
            robot = new Robot();
        } catch (java.awt.AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void show() {
        if (isRunning) return;
        isRunning = true;
        stage.show();
        startLoop();
    }

    public void hide() {
        isRunning = false;
        stage.hide();
    }

    private void startLoop() {
//        new Thread(() -> {
//            while (isRunning) {
//                try {
//                    // Получаем координаты курсора на экране
//                    Point mouse = robot.();
//
//                    if (mouse == null) {
//                        Thread.sleep(50);
//                        continue;
//                    }
//
//                    int x = (int) stage.getX();
//                    int y = (int) stage.getY();
//
//                    // Область для захвата: 25×25 пикселей (чтобы после ×4 было 100×100)
//                    int size = 25;
//                    Rectangle2D captureRect = new Rectangle2D(x - size / 2, y - size / 2, size, size);
//
//                    // Захват экрана (Java AWT)
//                    java.awt.Rectangle awtRect = new java.awt.Rectangle(
//                            (int) captureRect.getMinX(),
//                            (int) captureRect.getMinY(),
//                            (int) captureRect.getWidth(),
//                            (int) captureRect.getHeight()
//                    );
//                    java.awt.image.BufferedImage bufferedImage = robot.createScreenCapture(awtRect);
//
//                    // Конвертируем в JavaFX Image
//                    javafx.scene.image.Image fxImage = new javafx.scene.image.Image(
//                            bufferedImage.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH)
//                                    .getSource() // упрощённо: можно сделать через PixelWriter, но это быстрее для примера
//                    );
//
//                    // Более корректный способ конвертации (без потери качества и без getSource)
//                    fxImage = convertBufferedImageToFXImage(bufferedImage);
//
//                    imageView.setImage(fxImage);
//
//                    // Позиционируем окно лупы рядом с курсором
//                    stage.setX(mouse.x + 20);
//                    stage.setY(mouse.y + 20);
//
//                    Thread.sleep(30); // 30 мс ~ 33 FPS
//                } catch (InterruptedException | java.awt.AWTException e) {
//                    isRunning = false;
//                }
//            }
//        }).start();
    }

}
