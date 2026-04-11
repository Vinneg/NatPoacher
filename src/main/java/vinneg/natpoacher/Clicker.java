package vinneg.natpoacher;

import vinneg.natpoacher.Seeker.Mass;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.System.currentTimeMillis;

public class Clicker {

    private static final double speed = 1080D / 1000;

    private final Robot robot;
    private final Rectangle area;

    public Clicker(int x, int y, int width, int height) throws AWTException {
        this.robot = new Robot();
        this.area = new Rectangle(x, y, width, height);
    }

    public Mass move(Mass bundle) {
        move((int) bundle.x + area.x, (int) bundle.y + area.y);

        return bundle;
    }

    private void move(int ex, int ey) {
        Point cp = MouseInfo.getPointerInfo()
                .getLocation();
        double x = cp.getX();
        double y = cp.getY();

        final long et = currentTimeMillis() + (long) (Seeker.dist(ex - x, ey - y) / speed);

        for (long t = 0; t < et; t = currentTimeMillis()) {
            long dt = et - t;

            x = x + (ex - x) / dt;
            y = y + (ey - y) / dt;

            robot.mouseMove((int) x, (int) y);
        }

        robot.mouseMove(ex, ey);
    }

    public BufferedImage screen() {
        return robot.createScreenCapture(area);
    }

    public BufferedImage bobber(Bobber bobber) {
        return robot.createScreenCapture(bobber.area);
    }

    public void delay(int ms) {
        robot.delay(ms);
    }

    public void click() {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(std());
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    public void key(int key) {
        robot.keyPress(key);
        robot.delay(std());
        robot.keyRelease(key);
    }

    private int std() {
        return ThreadLocalRandom.current()
                .nextInt(25, 201);
    }

}
