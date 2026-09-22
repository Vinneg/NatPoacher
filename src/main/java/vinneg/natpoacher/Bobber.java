package vinneg.natpoacher;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bobber {

    public static final int SIDE = 2 * Seeker.R + 1;

    public final Clicker clicker;

    public final int x;
    public final int y;
    public final Rectangle area;
    public long redness;
    private static int delta = 4_500;

    public Bobber(Clicker clicker) {
        this.clicker = clicker;

        Point cp = MouseInfo.getPointerInfo()
                .getLocation();

        this.x = cp.x;
        this.y = cp.y;

        this.area = new Rectangle(x - Seeker.R, y - Seeker.R, SIDE, SIDE);

        redness = getRedness();
    }

    public static void decDelta() {
        delta -= delta > 12 ? 1 : 0;
        System.out.println("Bobber delta = " + delta);
    }

    private long getRedness() {
        BufferedImage image = clicker.bobber(this);

        long ttl = 0;

        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;

                if (Seeker.test(rgb)) {
                    ttl += red;
                }
            }
        }

        return ttl;
    }

    public boolean still() {
        long cur = getRedness();
        long diff = cur - redness;
        boolean res = -delta <= diff && diff <= delta;

        if (!res) {
            redness = cur;
            System.out.println("Bobber triggered with redness " + cur + " and diff " + diff);
        }

        return res;
    }

}
