package vinneg.natpoacher;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bobber {

    public static final int SIDE = 2 * Seeker.R + 1;
    public static final int SQUARE = Seeker.square(SIDE);
    private static final int DIFF = 4;

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

        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                int rgb = image.getRGB(x, y);
                ttl += (rgb >> 16) & 0xFF;
            }
        }

        return (int) (ttl / SQUARE);
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
