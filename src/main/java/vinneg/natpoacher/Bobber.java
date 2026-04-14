package vinneg.natpoacher;

import java.awt.*;
import java.awt.image.BufferedImage;

import static vinneg.natpoacher.Seeker.RATIO;
import static vinneg.natpoacher.Seeker.THRESHOLD;

public class Bobber {

    public static final int SIDE = 2 * Seeker.R + 1;
    public static final int SQUARE = Seeker.square(SIDE);
    private static final int DIFF = 10;

    public final Clicker clicker;

    public final int x;
    public final int y;
    public final Rectangle area;
    public int redness;

    public Bobber(Clicker clicker) {
        this.clicker = clicker;

        Point cp = MouseInfo.getPointerInfo()
                .getLocation();

        this.x = cp.x;
        this.y = cp.y;

        this.area = new Rectangle(x - Seeker.R, y - Seeker.R, SIDE, SIDE);

        redness = getRedness();
    }

    private int getRedness() {
        BufferedImage image = clicker.bobber(this);

        long ttl = 0;
        long c = 0;

        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                if (red > THRESHOLD && red > green * RATIO && red > blue * RATIO) {
                    ttl += red;
                    c++;
                }
            }
        }

        return (int) (ttl / c);
    }

    public boolean still() {
        int cur = getRedness();
        int diff = cur - redness;
        boolean res = -DIFF <= diff && diff <= DIFF;

        if (!res) {
            System.out.println("Bobber triggered with redness " + cur);
        }

        return res;
    }

}
