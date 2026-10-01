package vinneg.natpoacher;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

import static vinneg.natpoacher.Bobber.SIDE;

public class Magnifier implements Runnable {

    private static Thread main;

    private Stage magni;
    private ImageView view;
    private final Robot robot;

    public Magnifier(Stage magni, ImageView view) throws AWTException {
        this.magni = magni;
        this.view = view;
        this.robot = new Robot();
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            int x = (int) magni.getX();
            int y = (int) magni.getY();

            Rectangle rect = new Rectangle(x + SIDE, y + SIDE, SIDE * 2, SIDE * 2 + 1);

            BufferedImage img = robot.createScreenCapture(rect);
//            img.getScaledInstance(SIDE * 2, SIDE * 2, Image.SCALE_SMOOTH)
            javafx.scene.image.Image scaled = SwingFXUtils.toFXImage(img, null);
            view.setImage(scaled);
        }
    }

    public static void start(Stage magni, ImageView view) throws AWTException {
        if (main == null) {
            Magnifier magnifier = new Magnifier(magni, view);

            main = new Thread(magnifier, UUID.randomUUID().toString());
        }

        main.start();
    }

    public static void stop() {
        if (main == null) {
            return;
        }

        try {
            main.interrupt();
            main.join(3 * 1_000); // ждём до 3 сек
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            main = null;
        }
    }

}
