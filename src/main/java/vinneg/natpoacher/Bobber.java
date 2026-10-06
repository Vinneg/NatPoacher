package vinneg.natpoacher;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import vinneg.natpoacher.Seeker.Mass;

import java.awt.*;
import java.awt.image.BufferedImage;

import static vinneg.natpoacher.Log.log;

public class Bobber {

    public static final int SIDE = 2 * Seeker.R + 1;

    public final Clicker clicker;
    public final ImageView peep;

    public final int x;
    public final int y;
    public final Rectangle area;
    public Mass mass;

    public Bobber(Clicker clicker, ImageView peep) {
        this.clicker = clicker;
        this.peep = peep;

        Point cp = MouseInfo.getPointerInfo()
                .getLocation();

        this.x = cp.x;
        this.y = cp.y;

        this.area = new Rectangle(x - Seeker.R, y - Seeker.R, SIDE, SIDE);

        mass = getRedness();
    }

    private Mass getRedness() {
        BufferedImage image = clicker.bobber(this);
        peep.setImage(SwingFXUtils.toFXImage(image, null));

        int m = 0;
        long xm = 0;
        long ym = 0;

        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (Seeker.test(image.getRGB(x, y))) {
                    m++;
                    xm += x;
                    ym += y;
                }
            }
        }

        return new Mass((double) xm / m, (double) ym / m, m);
    }

    public boolean still() {
        Mass cur = getRedness();

        double r = ((double) cur.m) / mass.m;
        boolean resR = 0.5 <= r && r <= 1.5;
        if (!resR) {
            log("Bobber triggered with redness %f", r);
        }

        double d = cur.dist(mass);
        boolean resD = d < Seeker.R;
        if (!resD) {
            log("Bobber triggered with mass %f", d);
        }

        mass = cur;

        return resR || resD;
    }

}
